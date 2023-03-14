package io.eagle.domain.order.service;

import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import io.eagle.domain.order.dto.request.StockDto;
import io.eagle.domain.order.dto.response.BroadcastStockDto;
import io.eagle.domain.order.dto.TotalMountDto;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Order;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import io.eagle.exception.SocketException;
import io.eagle.repository.UserRepository;
import io.eagle.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static io.eagle.entity.type.MarketRankingType.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final VacationRepository vacationRepository;
    private final PriceInfoRepository priceInfoRepository;
    private final RedisTemplate redisTemplate;
    private ZSetOperations<String, String> redisZSet;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${eagle.score.order}")
    private Integer orderScore;

    @PostConstruct
    public void init(){
        this.redisZSet = redisTemplate.opsForZSet();
    }

    @Transactional
    public BroadcastStockDto purchaseMarket(StockDto stockDto) {
        // 요청내역 검증
        StockDto verifiedStock = verifyStock(stockDto, OrderType.BUY);
        User user = verifyUser(
                userRepository.findById(verifiedStock.getRequesterId()).orElseThrow(()-> new SocketException("존재하지 않는 사용자입니다.")),
                verifiedStock
        );
        incrementInterestMarketScore(verifiedStock);

        //  order 확인해서 매도 수량 있는지 확인
        // TODO : 한번에 전체를 가져오지 않고 부분적으로 가져와 처리 후 모자라면 다시 그다음 리스트를 가져오도록 하기
        List<Order> sellingOrders = orderRepository.findAllByVacation(verifiedStock.getMarketId(), OrderType.SELL, verifiedStock.getPrice());
        Integer sellingOrderAmount = sellingOrders.stream().mapToInt(Order::getAmount).sum();
        log.debug("waiting order : {}", sellingOrderAmount);

        Order doneOrder = verifiedStock.buildOrder(user, vacationRepository.getReferenceById(verifiedStock.getMarketId()), sellingOrderAmount, OrderStatus.DONE);
        if (sellingOrderAmount > 0) { // 실 거래 가능
            orderRepository.save(doneOrder);
            Integer requestAmount = doneOrder.getAmount();
            for(Order sellingOrder: sellingOrders) { // 매도 별로 하나 씩 transaction 생성
                if(requestAmount <= 0) break;
                requestAmount -= processOrdering(OrderType.BUY, requestAmount, doneOrder, sellingOrder);
            }
        }

        if (verifiedStock.getAmount() > sellingOrderAmount) { // 나와있는 매도 물량 부족해 일부 요청 수량은 ongoing으로
            processLeftOrder(verifiedStock, user, sellingOrderAmount);
        }

        //   해당 가격의 수량 확인해서 전달
        TotalMountDto leftAmount = orderRepository.getCurrentOrderAmount(verifiedStock.getMarketId(), verifiedStock.getPrice(), verifiedStock.getOrderType());
        log.debug(leftAmount.toString());
        return BroadcastStockDto.builder()
                .marketId(verifiedStock.getMarketId())
                .price(verifiedStock.getPrice())
                .amount(leftAmount.getAmount())
                .orderType(leftAmount.getType())
                .build();
    }

    private StockDto verifyStock(StockDto stock, OrderType orderType){
        if(stock.getAmount() <= 0) throw new SocketException("요청 수량이 올바르지 않습니다.");
        if(stock.getPrice() <= 0) throw new SocketException("요청 가격이 올바르지 않습니다.");

        try {
            Long userId = jwtTokenProvider.getUserIdFromToken(stock.getToken());
            stock.setRequesterId(userId);
            stock.setOrderType(orderType);
            return stock;
        } catch(Exception e){
            throw new SocketException("요청자가 올바르지 않습니다.");
        }
    }

    private User verifyUser(User user, StockDto stock) {
        verifyUserCash(user.getCash(), stock);
        return subtractUserCash(user, stock);
    }

    private void verifyUserCash(Long userCash, StockDto stock){
        Integer totalPrice = stock.getPrice() * stock.getAmount();
        if (totalPrice > userCash) { // error handling
            throw new SocketException("사용자 캐시가 부족합니다");
        }
    }

    private User subtractUserCash(User user, StockDto stock){
        Long leftCash = user.getCash() - (stock.getAmount() * stock.getPrice());
        user.setCash(leftCash);
        return userRepository.save(user);
    }

    private Integer processOrdering(OrderType type, Integer requestAmount, Order purchaseOrder, Order saleOrder){
        Integer transactionAmount = getMin(requestAmount, purchaseOrder.getAmount(), saleOrder.getAmount());
        Order waitingOrder = (type.equals(OrderType.BUY) ? saleOrder : purchaseOrder);
        Order doneOrder;

        if (transactionAmount < waitingOrder.getAmount()) { // 대기 중인 매도가 요청 매수양 보다 많은 경우 쪼개기
            doneOrder = splitDoneOrder(waitingOrder, transactionAmount);
            waitingOrder.setAmount(waitingOrder.getAmount() - transactionAmount);
        } else { // 기존 tuple을 그대로 완료 처리
            waitingOrder.setStatus(OrderStatus.DONE);
            doneOrder = waitingOrder;
        }

        Transaction transaction = createTransaction(
                (doneOrder.getOrderType().equals(OrderType.BUY) ? doneOrder : purchaseOrder),
                (doneOrder.getOrderType().equals(OrderType.SELL) ? doneOrder : saleOrder),
                transactionAmount
        ); // transaction 생성

        // 최근 거래 휴양지 데이터 업데이트
        updateRecentTransactionDataInRedis(transaction);
        return transactionAmount;
    }

    public BroadcastStockDto sellMarket(StockDto message){
        return BroadcastStockDto.builder().build();
    }

    private void processLeftOrder(StockDto message, User user, Integer sellingOrderAmount){
        Order leftOrder = message.buildOrder(user, vacationRepository.getReferenceById(message.getMarketId()),message.getAmount() - sellingOrderAmount, OrderStatus.ONGOING);
        orderRepository.save(leftOrder);
    }

    public Order splitDoneOrder(Order order, Integer amount){
        Order doneSaleOrder = order.deepCopy();
        doneSaleOrder.setAmount(amount);
        doneSaleOrder.setStatus(OrderStatus.DONE);
        return orderRepository.save(doneSaleOrder);
    }

    public Integer getMin(Integer a, Integer b, Integer c){
        return List.of(a,b,c).stream().min(Integer::compare).orElse(0);
    }

    private Transaction createTransaction(Order purchaseOrder, Order saleOrder, Integer transactionAmount){
        return transactionRepository.save(Transaction.builder()
                .vacation(vacationRepository.getReferenceById(saleOrder.getVacation().getId()))
                .buyOrder(purchaseOrder)
                .sellOrder(saleOrder)
                .amount(transactionAmount)
                .price(saleOrder.getPrice())
                .build());
    }

    private void updateRecentTransactionDataInRedis(Transaction transaction) {
        Long vacationId = transaction.getVacation().getId();
        PriceInfo priceInfo = priceInfoRepository.findOneByVacationOrderByCreatedAt(vacationId);
        if (priceInfo != null) {
            Double price = (double) (priceInfo.getStandardPrice() - transaction.getPrice());
            Double priceRate = price * 100 / (double) transaction.getPrice();
            redisZSet.add(PRICE.getKey(), vacationId.toString(), price);
            redisZSet.add(PRICE_RATE.getKey(), vacationId.toString(), priceRate);
        }
    }

    private void incrementInterestMarketScore(StockDto stock){
        String key = CountryKey(vacationRepository.findById(stock.getMarketId()).get().getCountry());
        String value = stock.getMarketId().toString();
        Integer score = stock.getAmount() * orderScore;
        redisZSet.incrementScore(key, value, (double) score);
    }
}
