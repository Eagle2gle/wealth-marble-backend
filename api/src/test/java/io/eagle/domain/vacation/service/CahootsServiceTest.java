package io.eagle.domain.vacation.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.vacation.dto.request.CreateCahootsDto;
import io.eagle.domain.vacation.dto.response.DetailCahootsDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.*;
import io.eagle.entity.type.ThemeBuildingType;
import io.eagle.entity.type.ThemeLocationType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("공모 생성 확인")
    @Transactional
    public void createCahoots() {
        User owner = testUtil.createUser("anonymous", "anonymous@email.com");
        userRepository.save(owner);
        CreateCahootsDto createCahootsDto = CreateCahootsDto.builder()
                .title("준호네 집 테스트용")
                .description("우리집 준호를 다같이 이용합니다. 준호는 공공재입니다.")
                .expectedMonth(12)
                .expectedTotalCost(100000L)
                .country("대한민국")
                .location("경기도 성남시 분당구")
                .shortDescription("우리집 그 준호입니다.")
                .stockStart(LocalDate.of(2024,2,23))
                .stockEnd(LocalDate.of(2024,4,23))
                .stockNum(10)
                .stockPrice(12000L)
                .themeBuilding(ThemeBuildingType.GUEST_HOUSE)
                .themeLocation(ThemeLocationType.DOWNTOWN)
                .expectedRateOfReturn(120)
                .build();
        cahootsService.create(createCahootsDto, owner);
        Vacation vacation = vacationRepository.findByTitle(createCahootsDto.getTitle()).get();

        assertEquals(createCahootsDto.getDescription(), vacation.getDescription());
        assertEquals(createCahootsDto.getShortDescription(), vacation.getShortDescription());
        assertEquals(createCahootsDto.getStockPrice(), vacation.getStock().getPrice());
        assertEquals(owner.getEmail(), vacation.getUser().getEmail());

    }

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
