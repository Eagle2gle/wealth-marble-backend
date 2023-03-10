package io.eagle.domain.ContestParticipation.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.ContestParticipation.dto.HistoryCahootsDto;
import io.eagle.domain.ContestParticipation.dto.response.ContestParticipationMineDto;
import io.eagle.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.ContestParticipation;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.exception.ApiException;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static io.eagle.entity.type.VacationStatusType.CAHOOTS_ONGOING;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ContestParticipationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private ContestParticipationRepository contestParticipationRepository;

    @InjectMocks
    private ContestParticipationService contestParticipationService;

    private final TestUtil testUtil = new TestUtil();

    @Test
    @DisplayName("사용자 잔여 캐시 검증")
    public void verifyUserCash(){

        User user = testUtil.createUser("test", "test@email.com");
        Vacation vacation = testUtil.createVacation(user);

        when(vacationRepository.findByIdAndStatus(vacation.getId(), CAHOOTS_ONGOING)).thenReturn(Optional.of(vacation));

        Assertions.assertThrows(ApiException.class, ()-> {
            contestParticipationService.participate(vacation.getId(), 1000, user);
        });
    }

    @Test
    @DisplayName("공모 히스토리 전달")
    public void getHistory(){
        User user = testUtil.createUser("test", "test@email.com");
        Vacation vacation = testUtil.createVacation(user);
        ContestParticipation contestParticipation = testUtil.createContestParticipation(user, vacation);

        when(contestParticipationRepository.findAllByCahootsId(vacation.getId())).thenReturn(List.of(contestParticipation));
        List<HistoryCahootsDto> historyCahootsDto = contestParticipationService.getHistory(vacation.getId());

        assertEquals(historyCahootsDto.size(), 1);
        assertEquals(historyCahootsDto.get(0).getStocks(), contestParticipation.getStocks());
    }

    @Test
    @DisplayName("내_공모내역_가져오기")
    public void getMyContestParticipation() {
        // given
        User user = testUtil.createUser("test", "test@email.com");
        Vacation vacation = testUtil.createVacation(user);
        ContestParticipation contestParticipation = testUtil.createContestParticipation(user, vacation);
        ContestParticipationMineDto contestParticipationMineDto = ContestParticipationMineDto
            .builder()
            .title(vacation.getTitle())
            .createdAt(contestParticipation.getCreatedAt())
            .amount(contestParticipation.getStocks())
            .price(vacation.getStock().getPrice().intValue())
            .status(vacation.getStatus().toString())
            .build();

        // when
        when(contestParticipationRepository.findAllByUserId(user.getId())).thenReturn(List.of(contestParticipation));
        List<ContestParticipationMineDto> mineDtos = contestParticipationService.getMyContestParticipation(user);

        // then
        ContestParticipationMineDto dto = mineDtos.get(0);
        assertEquals(dto.getTitle(), contestParticipationMineDto.getTitle());
        assertEquals(dto.getAmount(), contestParticipationMineDto.getAmount());
        assertEquals(dto.getStatus(), contestParticipationMineDto.getStatus());
    }

}
