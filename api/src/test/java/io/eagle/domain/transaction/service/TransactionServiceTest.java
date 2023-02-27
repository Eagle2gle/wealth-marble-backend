package io.eagle.domain.transaction.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.PriceInfo.repository.PriceInfoRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.dto.request.RecentTransactionRequestDto;
import io.eagle.domain.transaction.dto.response.RecentTransactionResponseDto;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.PriceInfo;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    PriceInfoRepository priceInfoRepository;
    @Mock
    VacationRepository vacationRepository;
    @Mock
    PictureRepository pictureRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    @DisplayName("최근_거래내역_서비스_제작")
    void createRecentTransactionResponseDto() {
        // given
        TestUtil testUtil = new TestUtil();
        RecentTransactionRequestDto request = this.transactionRequestDto();
        User user = testUtil.createUser("test", "test@email.com");
        Vacation vacation = testUtil.createVacation(user);
        PriceInfo priceInfo = testUtil.createPriceInfo(vacation);

        // when
        Mockito.when(vacationRepository.findById(vacation.getId())).thenReturn(Optional.of(vacation));
        Mockito.when(pictureRepository.findUrlsByCahootsId(vacation.getId())).thenReturn(null);
        Mockito.when(priceInfoRepository.findOneByVacationId(vacation.getId())).thenReturn(priceInfo);
        RecentTransactionResponseDto result = transactionService.createRecentTransactionResponseDto(request);

        // then
        Assertions.assertEquals(result.getTitle(), vacation.getTitle());
        Assertions.assertEquals(result.getCurrentPrice(), request.getCurrentPrice());
        Assertions.assertEquals(result.getGap(), request.getCurrentPrice() - priceInfo.getStandardPrice());
        Assertions.assertEquals(result.getDividend(), request.getCurrentPrice() * 0.1);
        Assertions.assertEquals(result.getCreatedAt(), request.getCreatedAt());
    }

    private RecentTransactionRequestDto transactionRequestDto() {
        return RecentTransactionRequestDto.builder()
            .vacationId(1L)
            .currentPrice(1000)
            .createdAt(LocalDateTime.now())
            .build();
    }

}
