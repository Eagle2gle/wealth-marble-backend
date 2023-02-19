package io.eagle.chunk.processor;

import io.eagle.domain.RetrieveMoneyVO;
import io.eagle.entity.ContestParticipation;
import io.eagle.entity.Vacation;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class RetrieveMoneyProcessor implements ItemProcessor<Vacation, List<RetrieveMoneyVO>> {

    private JdbcTemplate jdbcTemplate;

    public RetrieveMoneyProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<RetrieveMoneyVO> process(Vacation item) throws Exception {
        return this.findAllContestParticipationByVacation(item.getId()).stream().map(contestParticipation -> RetrieveMoneyVO.builder()
            .userId(contestParticipation.getUser().getId())
            .addCash(item.getStock().getPrice().intValue() * contestParticipation.getStocks())
            .contestParticipationId(contestParticipation.getId())
            .build()).collect(Collectors.toList());
    }

    private List<ContestParticipation> findAllContestParticipationByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select * from contest_participation where vacation_id = ?",
            new BeanPropertyRowMapper<>(ContestParticipation.class),
            vacationId
        );
    }

}
