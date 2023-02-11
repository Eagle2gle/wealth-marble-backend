package io.eagle.domain.order.service;

import io.eagle.common.TestUtil;
import io.eagle.config.TestConfig;
import io.eagle.domain.order.dto.MessageDto;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderType;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
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
    List<Order> buyOrders = new ArrayList<Order>();
    List<Order> sellOrders = new ArrayList<Order>();
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


    @Test
    @DisplayName("매수 요청. (매수 수량 < 매도 수량)")
    @org.junit.jupiter.api.Order(1)
    @Transactional
    void findAllByOrder() {
        // given
        createObject();
        sellOrders.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1719, 5, OrderType.SELL)));
        sellOrders.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1719, 7, OrderType.SELL)));
        sellOrders.add(orderRepository.save(testUtil.createOrder(seller, vacation, 1780, 3, OrderType.SELL)));

        // when
        MessageDto message = MessageDto.builder().marketId(vacation.getId()).price(1719).amount(6).build();
        orderService.purchaseMarket(message);

        // then
//        Order buyOrder = orderRepository.findAllByUser(userRepository.getReferenceById(1L));
//        transaction = transactionRepository.findOne();
//        assertEquals(transaction, findTransaction.getId());
    }
}
