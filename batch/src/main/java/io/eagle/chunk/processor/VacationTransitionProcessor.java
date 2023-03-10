package io.eagle.chunk.processor;

import io.eagle.entity.ContestParticipation;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static io.eagle.entity.type.VacationStatusType.*;

public class VacationTransitionProcessor implements ItemProcessor<Vacation, Vacation> {

    private JdbcTemplate jdbcTemplate;

    public VacationTransitionProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Vacation process(Vacation vacation) throws Exception {
        if(vacation.getStatus() == CAHOOTS_BEFORE){
            return this.changeToOngoing(vacation);
        }
        return this.isParticipationSuccess(vacation);
    }

    private Vacation changeToOngoing(Vacation vacation){
        vacation.setStatus(CAHOOTS_ONGOING);
        return vacation;
    }

    private Vacation isParticipationSuccess(Vacation vacation) {
        List<ContestParticipation> contestParticipations = this.findAllContestParticipationByVacation(vacation.getId());
        Integer totalAmount = this.calculateContestParticipationAmount(contestParticipations);

        if (totalAmount >= vacation.getStock().getNum()) {
                vacation.setStatus(MARKET_ONGOING);
                return vacation;
        }
        vacation.setStatus(CAHOOTS_CLOSE);
        return vacation;
    }

    private Integer calculateContestParticipationAmount(List<ContestParticipation> contestParticipations) {
        return contestParticipations.stream().mapToInt(ContestParticipation::getStocks).sum();
    }

    private List<ContestParticipation> findAllContestParticipationByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select * from contest_participation as c where c.cahoots_id = ?",
            new BeanPropertyRowMapper<>(ContestParticipation.class),
            vacationId
        );
    }
}
