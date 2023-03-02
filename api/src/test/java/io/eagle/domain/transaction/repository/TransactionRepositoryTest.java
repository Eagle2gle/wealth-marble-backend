package io.eagle.domain.transaction.repository;

import io.eagle.common.TestUtil;
import io.eagle.config.TestConfig;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.transaction.dto.request.TransactionRequestDto;
import io.eagle.domain.transaction.dto.response.TransactionResponseDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
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

    User owner;
    User buyer;
    User seller;
    Vacation vacation;
    Order buyOrder;
    Order sellOrder;
    Transaction transaction;

    void createObject() {
        TestUtil testUtil = new TestUtil();
        owner = testUtil.createUser("owner", "owner@email.com");
        buyer = testUtil.createUser("buyer", "buyer@email.com");
        seller = testUtil.createUser("seller", "seller@email.com");
        userRepository.saveAll(List.of(owner, buyer, seller));

        vacation = vacationRepository.save(testUtil.createVacation(owner));
        buyOrder = orderRepository.save(testUtil.createOrder(buyer, vacation, OrderType.BUY));
        sellOrder = orderRepository.save(testUtil.createOrder(seller, vacation, OrderType.SELL));

        transaction = testUtil.createTransaction(vacation, buyOrder, sellOrder);
        transactionRepository.save(transaction);
    }

    @Test
    @DisplayName("Order로_조회")
    @Transactional
    void findAllByOrder() {
        // given
        createObject();

        // when
        List<Transaction> transactions = transactionRepository.findAllByOrder(buyOrder);

        // then
        Transaction findTransaction = transactions.get(0);
        assertNotNull(transactions);
        assertEquals(transaction.getId(), findTransaction.getId());
        assertEquals(transaction.getBuyOrder().getId(), findTransaction.getBuyOrder().getId());
    }

    @Test
    @DisplayName("Vacation으로_조회")
    @Transactional
    void findByVacation() {
        // given
        createObject();

        // when
        Transaction findTransaction = transactionRepository.findOneByVacation(vacation.getId());

        // then
        assertNotNull(findTransaction);
        assertEquals(findTransaction.getId(), transaction.getId());
        assertEquals(findTransaction.getVacation().getId(), transaction.getVacation().getId());
    }

    @Test
    @DisplayName("특정_위치에_존재하는_page_조회")
    @Transactional
    void findByVacationOrderByCreatedAtDesc() {
        // given
        createObject();

        // when
        PageRequest page = PageRequest.of(0, 20);
        TransactionRequestDto requestDto = TransactionRequestDto.builder()
            .vacationId(vacation.getId())
            .startDate(LocalDate.now().minusDays(1))
            .endDate(LocalDate.now())
            .build();
        List<TransactionResponseDto> responseDtos = transactionRepository.findByVacationOrderByCreatedAtDesc(page, requestDto);

        // then
        TransactionResponseDto responseDto = responseDtos.get(0);
        assertNotNull(responseDtos);
        assertEquals(responseDto.getAmount(), transaction.getAmount());
        assertEquals(responseDto.getPrice(), transaction.getPrice());
    }

}
