package io.eagle.domain.interest.service;

import io.eagle.common.TestUtil;
import io.eagle.domain.interest.dto.InterestDto;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    
}
