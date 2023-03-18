package io.eagle.domain.order.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.order.dto.request.StockDto;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.OrderType;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderProduceService orderProduceService;

    @Autowired
    private OrderConsumeService orderConsumeService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final TestUtil testUtil = new TestUtil();

    User owner;
    User buyer;
    User seller;
    Vacation vacation;
    List<Order> buyOrderList = new ArrayList<Order>();
    List<Order> sellOrderList = new ArrayList<Order>();
    Transaction transaction;

    void createObject() {
        TestUtil testUtil = new TestUtil();
        owner = testUtil.createUser("owner", "owner@email.com");
        buyer = testUtil.createUser("buyer", "buyer@email.com");
        seller = testUtil.createUser("seller", "seller@email.com");
        userRepository.saveAll(List.of(owner, buyer, seller));

        vacation = vacationRepository.save(testUtil.createVacation(owner));
        vacation.setStatus(VacationStatusType.CAHOOTS_ONGOING);

    }

    StockDto createMessage(Long vacationId, Long requesterId, Integer price, Integer amount){
        StockDto message = new StockDto();
        message.setPrice(price);
        message.setAmount(amount);
        message.setMarketId(vacationId);
        message.setRequesterId(requesterId);
        return message;
    }


    @Test
    @DisplayName("매수 요청. (매수 수량 < 매도 수량)")
    @org.junit.jupiter.api.Order(1)
    @Transactional
    void purchaseOrder1() {
        // given
        createObject();
        sellOrderList.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1719, 5, OrderType.SELL)));
        sellOrderList.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1719, 7, OrderType.SELL)));
        sellOrderList.add(orderRepository.save(testUtil.createOrder(owner, vacation, 1780, 3, OrderType.SELL)));

        // when
        StockDto message = createMessage(vacation.getId(), buyer.getId(), 1719, 6);
        orderService.purchaseMarket(message);

        // then
        List<Order> buyOrders = orderRepository.findAllByUser(buyer);
        assertEquals(buyOrders.size(), 1);
        for(Order order : buyOrders){
            assertEquals(order.getUser().getId() , buyer.getId());
            assertEquals(order.getVacation().getId(), message.getMarketId());
            assertEquals(order.getOrderType(), OrderType.BUY);
            assertEquals(order.getPrice(), message.getPrice());
            assertEquals(order.getStatus(), OrderStatus.DONE);
        }

        List<Order> sellOrders = orderRepository.findAllByUser(seller);
        Order doneSplitOrder;
        assertEquals(sellOrders.size(), 3);
        for(Order order : sellOrders){
            assertEquals(order.getPrice(),1719);
            if(order.getId().equals(sellOrderList.get(0).getId())){ // amount 5
                assertEquals(order.getAmount(),5);
                assertEquals(order.getStatus(), OrderStatus.DONE);
            } else if(order.getId().equals(sellOrderList.get(1).getId())) { // amount 7 -> 6
                assertEquals(order.getAmount(),6);
                assertEquals(order.getStatus(), OrderStatus.ONGOING);
            } else{
                doneSplitOrder = order;
                assertEquals(order.getAmount(),1);
                assertEquals(order.getStatus(), OrderStatus.DONE);
            }
        }

        List<Transaction> transactionList = transactionRepository.findByPrice(1719);
        assertEquals(transactionList.size(), 2);
        for(Transaction transaction : transactionList){
            assertEquals(transaction.getBuyOrder().getId(), buyOrders.get(0).getId());
            if(transaction.getAmount() == 5){
                assertEquals(transaction.getSellOrder().getId(), sellOrderList.get(0).getId());
            } else if (transaction.getAmount() == 1){
                assertEquals(transaction.getVacation().getId(), sellOrders.get(1).getVacation().getId());
            } else{
                fail();
            }
        }

    }

    @Test
    @DisplayName("매수 요청. (매수 수량 > 매도 수량)")
    @org.junit.jupiter.api.Order(2)
    @Transactional
    void purchaseOrder2() {
        // given
        createObject();
        sellOrderList.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1719, 5, OrderType.SELL)));
        sellOrderList.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1719, 7, OrderType.SELL)));
        sellOrderList.add(orderRepository.save(testUtil.createOrder(owner, vacation, 1780, 3, OrderType.SELL)));

        // when
        StockDto message = createMessage(vacation.getId(), buyer.getId(), 1719, 15);
        orderService.purchaseMarket(message);

        // then
        List<Order> buyOrders = orderRepository.findAllByUser(buyer);
        assertEquals(buyOrders.size(), 2);
        for(Order order : buyOrders){
            assertEquals(order.getUser().getId() , buyer.getId());
            assertEquals(order.getVacation().getId(), message.getMarketId());
            assertEquals(order.getOrderType(), OrderType.BUY);
            assertEquals(order.getPrice(), message.getPrice());
            if(order.getAmount() == 12){
                assertEquals(order.getStatus(), OrderStatus.DONE);
            } else if(order.getAmount() == 3){
                assertEquals(order.getStatus(), OrderStatus.ONGOING);
            } else{
                fail();
            }

        }

        List<Order> sellOrders = orderRepository.findAllByUser(seller);
        assertEquals(sellOrders.size(), 2);
        for(Order order : sellOrders){
            assertEquals(order.getPrice(),1719);
            assertEquals(order.getStatus(), OrderStatus.DONE);
            assertEquals(order.getVacation().getId(), message.getMarketId());
        }

        List<Transaction> transactionList = transactionRepository.findByPrice(1719);
        assertEquals(transactionList.size(), 2);
        for(Transaction transaction : transactionList){
            assertEquals(transaction.getBuyOrder().getId(), buyOrders.get(0).getId());
            if(transaction.getAmount() == 5){
                assertEquals(transaction.getVacation().getId(), sellOrders.get(0).getVacation().getId());
            } else if (transaction.getAmount() == 7){
                assertEquals(transaction.getVacation().getId(), sellOrders.get(1).getVacation().getId());
            } else{
                fail();
            }
        }

    }
}
