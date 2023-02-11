package io.eagle.domain.transaction.repository;

import io.eagle.common.TestUtil;
import io.eagle.config.TestConfig;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private OrderRepository orderRepository;

    User buyer;
    User seller;
    Vacation vacation;
    Order buyOrder;
    Order sellOrder;
    Transaction transaction;

    @BeforeAll
    void createObject() {
        TestUtil testUtil = new TestUtil();
        User owner = testUtil.createUser("owner", "owner@email.com");
        buyer = testUtil.createUser("buyer", "buyer@email.com");
        seller = testUtil.createUser("seller", "seller@email.com");
        userRepository.saveAll(List.of(owner, buyer, seller));

        vacation = testUtil.createVacation(owner);
        vacationRepository.save(vacation);

        buyOrder = testUtil.createOrder(buyer, vacation, OrderType.BUY);
        sellOrder = testUtil.createOrder(seller, vacation, OrderType.SELL);
        orderRepository.saveAll(List.of(buyOrder, sellOrder));

        transaction = testUtil.createTransaction(vacation, buyOrder, sellOrder);
        transactionRepository.save(transaction);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Order로_조회_테스트")
    void findAllByOrder() {
        // when
        List<Transaction> transactions = transactionRepository.findAllByOrder(buyOrder);

        // then
        Transaction findTransaction = transactions.get(0);
        assertNotNull(transactions);
        assertEquals(transaction.getBuyOrder().getId(), findTransaction.getBuyOrder().getId());
        assertEquals(transaction.getId(), findTransaction.getId());
        assertEquals(transaction.getVacationId(), findTransaction.getVacationId());
    }

}