package io.eagle.domain.order.service;

import io.eagle.domain.order.dto.MessageDto;
import io.eagle.domain.order.dto.BroadcastMessageDto;
import io.eagle.domain.order.dto.TotalMountDto;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import io.eagle.exception.SocketException;
import io.eagle.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public BroadcastMessageDto purchaseMarket(MessageDto message){
        verifyMessage(message);
        // TODO : 사용자 정보 요청에서 가져오기
        Long userId = 1L;
        User user = userRepository.findById(userId).orElseThrow(()-> new SocketException("존재하지 않는 사용자입니다."));
        verifyUserCash(user.getCash(), message); // 사용자 잔여 캐쉬 확인 (사려는 가격보다 부족하면 에러)
        subtractUserCash(user, message);

        //  order 확인해서 매도 수량 있는지 확인
        message.setOrderType(OrderType.BUY);
        // TODO : 한번에 전체를 가져오지 않고 부분적으로 가져와 처리 후 모자라면 다시 그다음 리스트를 가져오도록 하기
        List<Order> sellingOrders = orderRepository.findAllByVacation(message.getMarketId(), OrderType.SELL, message.getPrice());
        Integer sellingOrderAmount = sellingOrders.stream().mapToInt(Order::getAmount).sum();
        log.debug("waiting order : {}ea",sellingOrderAmount);
        Order doneOrder = message.buildOrder(user, sellingOrderAmount, OrderStatus.DONE);
        if(sellingOrderAmount > 0) { // 실 거래 가능
            orderRepository.save(doneOrder);
            Integer requestAmount = doneOrder.getAmount();
            for(Order sellingOrder: sellingOrders){ // 매도 별로 하나 씩 transaction 생성
                if(requestAmount <= 0) break;
                requestAmount -= processOrdering(OrderType.BUY, requestAmount, doneOrder, sellingOrder);
            }
        }

        if( message.getAmount() > sellingOrderAmount ) { // 나와있는 매도 물량 부족해 일부 요청 수량은 ongoing으로
            processLeftOrder(message, user, sellingOrderAmount);
        }

        //   해당 가격의 수량 확인해서 전달
        TotalMountDto leftAmount = orderRepository.getCurrentOrderAmount(message.getMarketId(),message.getPrice(), message.getOrderType());
        log.debug(leftAmount.toString());
        return BroadcastMessageDto.builder()
                .marketId(message.getMarketId())
                .price(message.getPrice())
                .amount(leftAmount.getAmount())
                .orderType(leftAmount.getType())
                .build();
    }

    private void verifyMessage(MessageDto message){
        if(message.getAmount() <= 0) throw new SocketException("요청 수량이 올바르지 않습니다.");
        if(message.getPrice() <= 0) throw new SocketException("요청 가격이 올바르지 않습니다.");
    }
    private void verifyUserCash(Integer userCash, MessageDto message){
        Integer totalPrice = message.getPrice() * message.getAmount();
        if(totalPrice > userCash){// error handling
            throw new SocketException("사용자 캐시가 부족합니다");
        }
    }

    private void subtractUserCash(User user, MessageDto message){
        Integer leftCash = user.getCash() - (message.getAmount() * message.getPrice());
        user.setCash(leftCash);
    }
    private Integer processOrdering(OrderType type, Integer requestAmount, Order purchaseOrder, Order saleOrder){
        Integer transactionAmount = getMin(requestAmount, purchaseOrder.getAmount(), saleOrder.getAmount());
        Order waitingOrder = (type.equals(OrderType.BUY) ? saleOrder : purchaseOrder);
        Order doneOrder;
        if(transactionAmount < waitingOrder.getAmount()){ // 대기 중인 매도가 요청 매수양 보다 많은 경우 쪼개기
            doneOrder = splitDoneOrder(waitingOrder, transactionAmount);
            waitingOrder.setAmount(waitingOrder.getAmount() - transactionAmount);
        } else{ // 기존 tuple을 그대로 완료 처리
            waitingOrder.setStatus(OrderStatus.DONE);
            doneOrder = waitingOrder;
        }

        createTransaction(
                (doneOrder.getOrderType().equals(OrderType.BUY) ? doneOrder : purchaseOrder),
                (doneOrder.getOrderType().equals(OrderType.SELL) ? doneOrder : saleOrder),
                transactionAmount
        ); // transaction 생성
        return transactionAmount;
    }

    public BroadcastMessageDto sellMarket(MessageDto message){


        return BroadcastMessageDto.builder().build();
    }

    private void processLeftOrder(MessageDto message,User user, Integer sellingOrderAmount){
        Order leftOrder = message.buildOrder(user, message.getAmount() - sellingOrderAmount, OrderStatus.ONGOING);
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
        // transaction 생성
        Transaction transaction = Transaction.builder()
                .vacationId(saleOrder.getVacationId())
                .buyOrder(purchaseOrder)
                .sellOrder(saleOrder)
                .amount(transactionAmount)
                .price(saleOrder.getPrice())
                .build();
        transactionRepository.save(transaction);
    }
}