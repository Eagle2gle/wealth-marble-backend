package io.eagle.domain.vacation.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.vacation.dto.response.DetailCahootsDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.*;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class CahootsServiceTest {

    @Autowired
    private CahootsService cahootsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private ContestParticipationRepository contestParticipationRepository;

    @Autowired
    private InterestRepository interestRepository;

    private final TestUtil testUtil = new TestUtil();

    @Test
    @DisplayName("공모 상세 정보")
    @Transactional
    void getVacationInfo() {
        // given
        User owner = testUtil.createUser("anonymous", "anonymous@email.com");
        User contributor = testUtil.createUser("contributor", "contributor@email.com");
        userRepository.saveAll(List.of(owner, contributor));
        Vacation vacation = testUtil.createVacation(owner);
        vacationRepository.save(vacation);
        interestRepository.save(new Interest(contributor, vacation));
        Integer stocks = 7;
        contestParticipationRepository.save(new ContestParticipation(contributor, vacation, stocks));

        // when
        DetailCahootsDto detailCahootsDto = cahootsService.getDetail(vacation.getId(), contributor.getId());

        // then
        assertEquals(detailCahootsDto.getDescription(), vacation.getDescription());
        assertEquals(detailCahootsDto.getInterestCount(), 1);
        assertEquals(detailCahootsDto.getStockPrice(), vacation.getStock().getPrice());
        assertEquals(detailCahootsDto.getCompetitionRate(), Math.round(stocks*100/vacation.getStock().getNum()));

    }

}
