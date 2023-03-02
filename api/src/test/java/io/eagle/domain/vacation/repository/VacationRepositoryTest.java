package io.eagle.domain.vacation.repository;

import io.eagle.common.TestUtil;
import io.eagle.config.TestConfig;
import io.eagle.domain.vacation.dto.InfoConditionDto;
import io.eagle.domain.vacation.dto.response.BreifCahootsDto;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.eagle.entity.type.VacationStatusType.CAHOOTS_ONGOING;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VacationRepositoryTest {
    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private UserRepository userRepository;

    private final TestUtil testUtil = new TestUtil();

    @Test
    @Order(1)
    @DisplayName("열린 공모 목록 전달")
    public void getBriefList(){
        User owner = testUtil.createUser("anonymous", "anonymous@email.com");
        userRepository.save(owner);
        Vacation vacation = testUtil.createVacation(owner);
        vacation.setStatus(CAHOOTS_ONGOING);
        vacationRepository.save(vacation);

        List<BreifCahootsDto> breifCahootsDtoList = vacationRepository.getVacationsBreif(InfoConditionDto.builder()
                .page(0)
                .types(new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING})
                .keyword("다나카 테스트 휴양지")
                .build()
        );

        assertEquals(breifCahootsDtoList.size(), 1);
        breifCahootsDtoList.forEach(e->{
            assertEquals(e.getStatus(), CAHOOTS_ONGOING);
        });
    }
}
