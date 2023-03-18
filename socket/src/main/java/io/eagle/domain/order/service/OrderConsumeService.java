package io.eagle.domain.order.service;

import io.eagle.aop.DistributeLock;
import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import io.eagle.domain.order.dto.StockVO;
import io.eagle.domain.order.dto.TotalMountDto;
import io.eagle.domain.order.dto.response.BroadCastOrderDto;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.stock.repository.StockRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.*;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import io.eagle.exception.SocketException;
import io.eagle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.eagle.entity.type.MarketRankingType.PRICE;
import static io.eagle.entity.type.MarketRankingType.PRICE_RATE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderConsumeService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final VacationRepository vacationRepository;
    private final PriceInfoRepository priceInfoRepository;
    private final StockRepository stockRepository;
    private final RedisTemplate redisTemplate;
    private ZSetOperations<String, String> redisZSet;

    @PostConstruct
    public void init(){
        this.redisZSet = redisTemplate.opsForZSet();
    }

    @DistributeLock(key = "#marketId")
    public BroadCastOrderDto createTransactionsByOrder(Long marketId, StockVO stockVO) {
        List<Order> anotherContrastOrders = orderRepository.findAllByVacation(
            marketId,
            stockVO.getOrderType().equals(OrderType.BUY) ? OrderType.SELL : OrderType.BUY,
            stockVO.getPrice()
        );
        Integer anotherOrderAmount = anotherContrastOrders.stream().mapToInt(Order::getAmount).sum();

        // 3가지 구매 케이스
        // 1. 판매 수량과 일치 -> 판매 Order, 구매 Order 모두 Done 으로 수정
        // 2. 판매 수량보다 적을 때 -> 판매 Order 의 일부를 차감하고, 구매 Order 자체를 Done으로 수정 Transaction으로 전환
        // 3. 판매 수량보다 많을 때 -> 판매 Order 전체를 Done으로 수정, 구매 Order의 일부만 Done으로 수정하고 (ongoing의 )amount를 차감 Transaction으로 전환

        Order myOrder = orderRepository.findById(stockVO.getOrderId()).orElseThrow(()-> new SocketException("존재 하지 않는 주문 id 입니다."));
        if (anotherOrderAmount > 0) {
            if (stockVO.getAmount().equals(anotherOrderAmount)) {
                sameAmountOrderProcess(myOrder, anotherContrastOrders);
            } else if (stockVO.getAmount() < anotherOrderAmount) {
                lessAmountOrderProcess(myOrder, anotherContrastOrders);
            } else {
                largerAmountOrderProcess(myOrder, anotherContrastOrders, anotherOrderAmount);
            }
        }

        //   해당 가격의 수량 확인해서 전달
        TotalMountDto leftAmount = orderRepository.getCurrentOrderAmount(marketId, stockVO.getPrice(), stockVO.getOrderType());
        log.debug(leftAmount != null ? leftAmount.toString(): "No left any more sell done!");
        return BroadCastOrderDto.builder()
            .marketId(stockVO.getMarketId())
            .price(stockVO.getPrice())
            .amount(leftAmount != null ? leftAmount.getAmount() : 0)
            .orderType( leftAmount != null ? leftAmount.getType() : stockVO.getOrderType())
            .build();
    }

    private void sameAmountOrderProcess(Order myOrder, List<Order> anotherOrders) {
        List<Order> completeOrder = createTransactionStepByStep(myOrder, anotherOrders);

        // 내 주문 상태도 변경
        myOrder.setStatus(OrderStatus.DONE);
        completeOrder.add(myOrder);
        orderRepository.saveAll(completeOrder);
    }

    private void lessAmountOrderProcess(Order myOrder, List<Order> anotherOrders){
        myOrder.setStatus(OrderStatus.DONE);
        List<Order> completeOrder = new ArrayList<>();
        completeOrder.add(myOrder);

        // another order 순차 적용하면서 transaction 생성
        int myLeftAmount = myOrder.getAmount();
        for (Order anotherOrder : anotherOrders) {
            Order doneOrder;

            // 구매 10개 , 판매로 나온게 3, 5, 10 -> anotherOrderAmount = 18
            if (myLeftAmount <= 0) {
                break;
            } else if (myLeftAmount < anotherOrder.getAmount()){ // 3개를 살건데 판매로 5개가 있을 때
                doneOrder = splitDoneOrder(anotherOrder, myLeftAmount); // Done은 3개만 발생, order 2개....
                anotherOrder.setAmount(anotherOrder.getAmount() - myLeftAmount); // 기존에 있는 건 잔여 2개로 변경
                myLeftAmount -= doneOrder.getAmount();
            } else { // 통째로 done으로 바꿔버림
                anotherOrder.setStatus(OrderStatus.DONE);
                doneOrder = anotherOrder;
                myLeftAmount -= anotherOrder.getAmount();
            }

            completeOrder.add(anotherOrder);
            createTransactionPerType(myOrder, doneOrder);
        }

        orderRepository.saveAll(completeOrder);
    }

    private void largerAmountOrderProcess(Order myOrder, List<Order> anotherOrders, Integer anotherOrderAmount) {
        //내가 생성한 주문 쪼개기
        Order myDoneOrder = splitDoneOrder(myOrder, anotherOrderAmount);
        myOrder.setAmount(myOrder.getAmount() - anotherOrderAmount);

        // 다른 사람 주문은 전부 체결 완료상태로 저장하면서 각각 transaction 생성
        List<Order> completeOrder = createTransactionStepByStep(myDoneOrder, anotherOrders);
        completeOrder.add(myOrder);
        orderRepository.saveAll(completeOrder);
    }

    private void createTransactionPerType(Order myOrder, Order doneOrder) {
        Boolean checkType = isMyBuyOrder(myOrder);
        if (checkType) {
            createTransaction(myOrder, doneOrder, doneOrder.getAmount());
        } else {
            createTransaction(doneOrder, myOrder, doneOrder.getAmount());
        }
    }

    private List<Order> createTransactionStepByStep(Order myOrder, List<Order> anotherOrders) {
        return anotherOrders.stream().map(order -> {
            order.setStatus(OrderStatus.DONE);
            createTransactionPerType(myOrder, order);
            return order;
        }).collect(Collectors.toList());
    }

    private Boolean isMyBuyOrder(Order myOrder) {
        return myOrder.getOrderType().equals(OrderType.BUY);
    }

    public Order splitDoneOrder(Order order, Integer amount){
        Order doneSaleOrder = order.deepCopy();
        doneSaleOrder.setAmount(amount);
        doneSaleOrder.setStatus(OrderStatus.DONE);
        return orderRepository.save(doneSaleOrder);
    }

    private Transaction createTransaction(Order purchaseOrder, Order saleOrder, Integer transactionAmount){
        Transaction transaction = transactionRepository.save(Transaction.builder()
            .vacation(vacationRepository.getReferenceById(saleOrder.getVacation().getId()))
            .buyOrder(purchaseOrder)
            .sellOrder(saleOrder)
            .amount(transactionAmount)
            .price(saleOrder.getPrice())
            .build());

        addCashToSaleUser(saleOrder.getUser().getId(), saleOrder.getPrice(), transactionAmount);
        addStockToPurchaseUser(purchaseOrder, transaction);
        updateRecentTransactionDataInRedis(transaction);
        return transaction;
    }

    private void addCashToSaleUser(Long userId, Integer currentPrice, Integer transactionAmount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new SocketException("존재하지 않는 사용자입니다."));
        user.setCash(user.getCash() + currentPrice * transactionAmount);
        userRepository.save(user);
    }

    private void addStockToPurchaseUser(Order purchaseOrder, Transaction transaction){
        Optional<Stock> stock = stockRepository.findByUserAndVacation(purchaseOrder.getUser(), purchaseOrder.getVacation());
        if(stock.isEmpty()){
            stockRepository.save(Stock.builder()
                .user(purchaseOrder.getUser())
                .vacation(purchaseOrder.getVacation())
                .price(transaction.getPrice())
                .amount(transaction.getAmount())
                .build());
        } else {
            stock.get().setAmount(stock.get().getAmount() + transaction.getAmount());
            stock.get().setPrice(stock.get().getPrice() + (transaction.getAmount() * transaction.getPrice()));
            stockRepository.save(stock.get());
        }
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
}
