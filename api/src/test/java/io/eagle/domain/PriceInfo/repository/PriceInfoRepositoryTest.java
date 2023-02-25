package io.eagle.domain.PriceInfo.repository;

import io.eagle.config.TestConfig;
import io.eagle.domain.PriceInfo.dto.ChartRequestDto;
import io.eagle.domain.PriceInfo.dto.ChartResponseDto;
import io.eagle.repository.UserRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.embeded.Plan;
import io.eagle.entity.embeded.Stock;
import io.eagle.entity.embeded.Theme;
import io.eagle.entity.type.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PriceInfoRepositoryTest {

    @Autowired
    private PriceInfoRepository priceInfoRepository;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("특정_Vacation의_PriceInfo_조회")
    void findAllByVacationOrderByCreatedAtTest() {
        // given
        User user = userRepository.save(createUser());
        Vacation vacation = vacationRepository.save(createVacation(user));
        PriceInfo priceInfo = priceInfoRepository.save(createPriceInfo(vacation));

        // when
        List<ChartResponseDto> result = priceInfoRepository.findAllByVacationOrderByCreatedAt(vacation.getId(), createChartRequestDto());

        // then
        ChartResponseDto res = result.get(0);
        assertEquals(res.getTransactionAmount(), priceInfo.getTransactionAmount());
        assertEquals(res.getPrice(), priceInfo.getStandardPrice());
        assertEquals(res.getDate(), priceInfo.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName("어제_요소_PriceInfo_조회")
    void getYesterdayPriceInfoTest() {
        // when
        PriceInfo priceInfo = priceInfoRepository.getYesterdayPriceInfo();

        // then
        assertNull(priceInfo);
    }

    PriceInfo createPriceInfo(Vacation vacation) {
        return PriceInfo
            .builder()
            .vacation(vacation)
            .standardPrice(10000)
            .highPrice(15000)
            .lowPrice(9000)
            .startPrice(8000)
            .transactionAmount(100)
            .transactionMoney(100000)
            .build();
    }

    Vacation createVacation(User user) {
        return Vacation
            .builder()
            .user(user)
            .status(VacationStatusType.CAHOOTS_CLOSE)
            .title("다나카와 함께하는")
            .theme(Theme.builder()
                .building(ThemeBuildingType.GUEST_HOUSE)
                .location(ThemeLocationType.BEACH)
                .build()
            )
            .location("부산")
            .plan(Plan.builder()
                .expectedMonth(12)
                .expectedTotalCost(30000L)
                .build()
            )
            .shortDescription("우리 휴양지에 놀러와요")
            .description("우리 휴양지에 놀러와요")
            .stockPeriod(Period.builder()
                .start(LocalDate.now().minusDays(1L))
                .end(LocalDate.now())
                .build()
            )
            .stock(Stock.builder()
                .price(10000L)
                .num(10)
                .build()
            )
            .expectedRateOfReturn(12)
            .build();
    }

    User createUser() {
        return User.builder()
            .nickname("구리")
            .email("jinsung1048@gmail.com")
            .providerType(ProviderType.GOOGLE)
            .providerId("12354126646")
            .ranks(Ranks.NAMJAK)
            .role(Role.USER)
            .cash(100L)
            .build();
    }

    ChartRequestDto createChartRequestDto() {
        return ChartRequestDto
            .builder()
            .startDate(LocalDate.now().minusDays(2))
            .endDate(LocalDate.now())
            .build();
    }

}
