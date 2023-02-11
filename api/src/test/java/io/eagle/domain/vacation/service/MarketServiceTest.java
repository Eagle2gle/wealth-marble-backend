package io.eagle.domain.vacation.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.dto.response.MarketInfoDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

}
