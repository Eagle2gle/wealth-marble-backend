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
    public BroadcastStockDto purchaseMarket(StockDto message){
        verifyMessage(message);
        User user = userRepository.findById(message.getRequesterId()).orElseThrow(()-> new SocketException("???????????? ?????? ??????????????????."));
        verifyUserCash(user.getCash(), message); // ????????? ?????? ?????? ?????? (????????? ???????????? ???????????? ??????)
        subtractUserCash(user, message);
        incrementInterestMarketScore(message);

        //  order ???????????? ?????? ?????? ????????? ??????
        message.setOrderType(OrderType.BUY);
        // TODO : ????????? ????????? ???????????? ?????? ??????????????? ????????? ?????? ??? ???????????? ?????? ????????? ???????????? ??????????????? ??????
        List<Order> sellingOrders = orderRepository.findAllByVacation(message.getMarketId(), OrderType.SELL, message.getPrice());
        Integer sellingOrderAmount = sellingOrders.stream().mapToInt(Order::getAmount).sum();
        log.debug("waiting order : {}ea",sellingOrderAmount);
        Order doneOrder = message.buildOrder(user, vacationRepository.getReferenceById(message.getMarketId()),sellingOrderAmount, OrderStatus.DONE);
        if(sellingOrderAmount > 0) { // ??? ?????? ??????
            orderRepository.save(doneOrder);
            Integer requestAmount = doneOrder.getAmount();
            for(Order sellingOrder: sellingOrders){ // ?????? ?????? ?????? ??? transaction ??????
                if(requestAmount <= 0) break;
                requestAmount -= processOrdering(OrderType.BUY, requestAmount, doneOrder, sellingOrder);
            }
        }

        if( message.getAmount() > sellingOrderAmount ) { // ???????????? ?????? ?????? ????????? ?????? ?????? ????????? ongoing??????
            processLeftOrder(message, user, sellingOrderAmount);
        }

        //   ?????? ????????? ?????? ???????????? ??????
        TotalMountDto leftAmount = orderRepository.getCurrentOrderAmount(message.getMarketId(),message.getPrice(), message.getOrderType());
        log.debug(leftAmount.toString());
        return BroadcastStockDto.builder()
                .marketId(message.getMarketId())
                .price(message.getPrice())
                .amount(leftAmount.getAmount())
                .orderType(leftAmount.getType())
                .build();
    }

    private void verifyMessage(StockDto message){
        try{
            Long userId = jwtTokenProvider.getUserIdFromToken(message.getToken());
            message.setRequesterId(userId);
        } catch(Exception e){
            throw new SocketException("???????????? ???????????? ????????????.");
        }
        if(message.getAmount() <= 0) throw new SocketException("?????? ????????? ???????????? ????????????.");
        if(message.getPrice() <= 0) throw new SocketException("?????? ????????? ???????????? ????????????.");
    }
    private void verifyUserCash(Long userCash, StockDto message){
        Integer totalPrice = message.getPrice() * message.getAmount();
        if(totalPrice > userCash){// error handling
            throw new SocketException("????????? ????????? ???????????????");
        }
    }

    private void subtractUserCash(User user, StockDto message){
        Long leftCash = user.getCash() - (message.getAmount() * message.getPrice());
        user.setCash(leftCash);
    }
    private Integer processOrdering(OrderType type, Integer requestAmount, Order purchaseOrder, Order saleOrder){
        Integer transactionAmount = getMin(requestAmount, purchaseOrder.getAmount(), saleOrder.getAmount());
        Order waitingOrder = (type.equals(OrderType.BUY) ? saleOrder : purchaseOrder);
        Order doneOrder;
        if(transactionAmount < waitingOrder.getAmount()){ // ?????? ?????? ????????? ?????? ????????? ?????? ?????? ?????? ?????????
            doneOrder = splitDoneOrder(waitingOrder, transactionAmount);
            waitingOrder.setAmount(waitingOrder.getAmount() - transactionAmount);
        } else{ // ?????? tuple??? ????????? ?????? ??????
            waitingOrder.setStatus(OrderStatus.DONE);
            doneOrder = waitingOrder;
        }

        createTransaction(
                (doneOrder.getOrderType().equals(OrderType.BUY) ? doneOrder : purchaseOrder),
                (doneOrder.getOrderType().equals(OrderType.SELL) ? doneOrder : saleOrder),
                transactionAmount
        ); // transaction ??????
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
        orderRepository.save(doneSaleOrder);
        return doneSaleOrder;
    }

    public Integer getMin(Integer a, Integer b, Integer c){
        return List.of(a,b,c).stream().min(Integer::compare).orElse(0);
    }

    private void createTransaction(Order purchaseOrder, Order saleOrder, Integer transactionAmount){
        Long vacationId = saleOrder.getVacation().getId();
        Transaction transaction = Transaction.builder()
                .vacation(vacationRepository.getReferenceById(vacationId))
                .buyOrder(purchaseOrder)
                .sellOrder(saleOrder)
                .amount(transactionAmount)
                .price(saleOrder.getPrice())
                .build();

        transactionRepository.save(transaction);
        PriceInfo priceInfo = priceInfoRepository.findByVacationOrderByCreatedAt(vacationId);

        if (priceInfo != null) {
            Double price = (double) (priceInfo.getStandardPrice() - saleOrder.getPrice());
            Double priceRate = price * 100 / (double) saleOrder.getPrice();
            redisZSet.add(PRICE.getKey(), vacationId.toString(), price);
            redisZSet.add(PRICE_RATE.getKey(), vacationId.toString(), priceRate);
        }

    }

    private void incrementInterestMarketScore( StockDto message){
        String key = CountryKey(vacationRepository.findById(message.getMarketId()).get().getCountry());
        String value = message.getMarketId().toString();
        Integer score = message.getAmount() * orderScore;
        redisZSet.incrementScore(key, value, (double) score);
    }
}
