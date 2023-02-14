package io.eagle.domain.interest.repository;

import io.eagle.common.TestUtil;
import io.eagle.config.TestConfig;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InterestRepositoryTest {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRepository vacationRepository;

    User user;
    Vacation vacation;
    Interest interest;

    void createObject() {
        TestUtil testUtil = new TestUtil();
        user = userRepository.save(testUtil.createUser("user", "user@email.com"));
        vacation = vacationRepository.save(testUtil.createVacation(user));
        interest = interestRepository.save(testUtil.createInterest(user, vacation));
    }

    @Test
    @DisplayName("사용자_interest_조회")
    @Transactional
    void findInterestByUser() {
        // given
        createObject();
        Pageable pageable = PageRequest.of(0, 20);

        // when
        List<Interest> interests = interestRepository.findInterestByUser(user, pageable);
        Interest findInterest = interests.get(0);

        // then
        assertEquals(findInterest.getId(), interest.getId());
        assertEquals(findInterest.getUser().getId(), interest.getUser().getId());
        assertEquals(findInterest.getVacation().getId(), interest.getVacation().getId());
    }

}
