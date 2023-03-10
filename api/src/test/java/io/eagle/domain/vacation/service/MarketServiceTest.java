package io.eagle.domain.vacation.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.dto.MarketInfoConditionDto;
import io.eagle.domain.vacation.dto.response.MarketInfoDto;
import io.eagle.domain.vacation.dto.response.MarketListDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Picture;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.PriceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
public class MarketServiceTest {

    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private MarketService marketService;

    @Test
    @DisplayName("휴양지_상세_정보_가져오기")
    void getVacationInfo() {
        // given
        TestUtil testUtil = new TestUtil();

        User user = testUtil.createUser("anonymous", "anonymous@email.com");
        Vacation vacation = testUtil.createVacation(user);
        Long vacationId = 1L;
        when(vacationRepository.findById(vacationId)).thenReturn(Optional.of(vacation));

        // when
        MarketInfoDto marketInfoDto = marketService.getVacationInfo(vacationId);

        // then
        assertEquals(marketInfoDto.getDescription(), vacation.getDescription());
        assertEquals(marketInfoDto.getLocation(), vacation.getLocation());
        assertEquals(marketInfoDto.getTitle(), vacation.getTitle());
    }

    @Test
    @DisplayName("휴양지_목록_가져오기")
    void getAllMarkets() {
        // given
        TestUtil testUtil = new TestUtil();

        User user = testUtil.createUser("anonymous", "anonymous@email.com");
        Vacation vacation = testUtil.createVacation(user);
        MarketInfoConditionDto marketInfoConditionDto = MarketInfoConditionDto.builder()
            .page(0)
            .size(10)
            .keyword("test")
            .build();
        Pageable pageable = PageRequest.of(0, 10);
        PriceInfo priceInfo = PriceInfo
            .builder()
            .vacation(vacation)
            .startPrice(1000)
            .lowPrice(100)
            .highPrice(10000)
            .standardPrice(5000)
            .transactionMoney(50000)
            .transactionAmount(10)
            .build();
        Picture picture = new Picture("test@rul", "test-type", vacation);
        vacation.setPictureList(List.of(picture));
        vacation.setPriceInfos(List.of(priceInfo));
        MarketListDto marketListDto = MarketListDto
            .builder()
            .priceStatus(PriceStatus.UP)
            .price(5000)
            .country(vacation.getCountry())
            .shortDescription(vacation.getShortDescription())
            .picture(picture.getUrl())
            .build();
        when(vacationRepository.findAllMarkets(pageable, marketInfoConditionDto.getKeyword())).thenReturn(List.of(vacation));

        // when
        List<MarketListDto> markets = marketService.getAllMarkets(marketInfoConditionDto);
        MarketListDto market = markets.get(0);

        // then
        assertEquals(market.getCountry(), marketListDto.getCountry());
        assertEquals(market.getShortDescription(), marketListDto.getShortDescription());
        assertEquals(market.getPrice(), marketListDto.getPrice());
        assertEquals(market.getPriceStatus(), marketListDto.getPriceStatus());
    }

}
