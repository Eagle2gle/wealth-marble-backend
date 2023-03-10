package io.eagle.domain.interest.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.interest.dto.InterestDto;
import io.eagle.domain.interest.dto.InterestInfoDto;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class InterestServiceTest {

    @Mock
    private InterestRepository interestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VacationRepository vacationRepository;

    @InjectMocks
    private InterestService interestService;

    @Test
    @DisplayName("내_관심상품_가져오기")
    void getAllInterest() {
        // given
        TestUtil testUtil = new TestUtil();
        User user = testUtil.createUser("anonymous", "anonymous@email.com");
        User marketUser = testUtil.createUser("market", "market@email.com");
        Vacation vacation = testUtil.createVacation(user);
        vacation.setStatus(VacationStatusType.MARKET_ONGOING);
        Interest interest = testUtil.createInterest(marketUser, vacation);
        Pageable pageable = Pageable.ofSize(10);
        String type = "market";

        // when
        when(interestRepository.findInterestByUser(marketUser, pageable)).thenReturn(List.of(interest));
        List<InterestInfoDto> interestDtos = interestService.getAllInterest(type, marketUser, pageable);

        // then
        InterestInfoDto infoDto = interestDtos.get(0);
        assertEquals(infoDto.getTitle(), vacation.getTitle());
        assertEquals(infoDto.getShortDescription(), vacation.getShortDescription());
        assertEquals(infoDto.getLocation(), vacation.getLocation());
        assertEquals(infoDto.getPicture(), vacation.getPictureList().get(0));
    }

    @Test
    @DisplayName("관심상품_생성하기")
    void createInterest() {
        // given
        TestUtil testUtil = new TestUtil();
        User user = testUtil.createUser("anonymous", "anonymous@email.com");
        User anotherUser = testUtil.createUser("interest", "interest@email.com");
        Vacation vacation = testUtil.createVacation(user);
        Interest interest = testUtil.createInterest(anotherUser, vacation);
        InterestDto interestDto = new InterestDto(1L, 1L);

        // when
        when(userRepository.getReferenceById(1L)).thenReturn(anotherUser);
        when(vacationRepository.getReferenceById(1L)).thenReturn(vacation);
        when(interestRepository.findByUserAndVacation(1L, 1L)).thenReturn(null);
        when(interestRepository.save(new Interest(anotherUser, vacation))).thenReturn(interest);
        Interest createInterest = interestService.createInterest(interestDto);

        // then
        assertEquals(createInterest.getUser(), interest.getUser());
        assertEquals(createInterest.getVacation(), interest.getVacation());
    }

    @Test
    @DisplayName("관심상품_삭제하기")
    void deleteInterest() {
        // given
        TestUtil testUtil = new TestUtil();
        User user = testUtil.createUser("anonymous", "anonymous@email.com");
        User anotherUser = testUtil.createUser("interest", "interest@email.com");
        Vacation vacation = testUtil.createVacation(user);
        Interest interest = testUtil.createInterest(anotherUser, vacation);
        InterestDto interestDto = new InterestDto(1L, 1L);

        // when
        when(interestRepository.findByUserAndVacation(1L, 1L)).thenReturn(interest);
        Boolean result = interestService.deleteInterest(interestDto);

        // then
        verify(interestRepository, times(1)).delete(interest);
        assertEquals(result, true);
    }

}
