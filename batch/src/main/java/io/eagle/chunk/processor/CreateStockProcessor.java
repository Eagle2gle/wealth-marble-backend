package io.eagle.chunk.processor;

import io.eagle.entity.*;
import io.eagle.entity.type.OrderStatus;
import io.eagle.entity.type.VacationStatusType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateStockProcessor implements ItemProcessor<Vacation, List<Stock>> {

    private JdbcTemplate jdbcTemplate;

    public CreateStockProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Stock> process(Vacation item) throws Exception {
        List<ContestParticipation> contestParticipations = this.findAllContestParticipationByVacation(item.getId());
        Integer total = this.totalAmount(contestParticipations);
        return this.assignStocks(contestParticipations, total, item);
    }

    private List<Stock> assignStocks(List<ContestParticipation> contestParticipations, Integer total, Vacation item) {
        List<Stock> stocks = new ArrayList<>();
        for (ContestParticipation contestParticipation: contestParticipations) {
            User user = contestParticipation.getUser();
            if (total <= 0) {
                stocks.add(this.createStock(user, item, 0));
            } else {
                Double amount = Math.ceil(contestParticipation.getStocks() * 100 / total);
                if (total <= amount.intValue()) {
                    stocks.add(this.createStock(user, item, total));
                    total = 0;
                } else {
                    stocks.add(this.createStock(user, item, amount.intValue()));
                    total -= amount.intValue();
                }
            }
        }
        return stocks;
    }

    private Integer totalAmount(List<ContestParticipation> contestParticipations) {
        return contestParticipations.stream().mapToInt(ContestParticipation::getStocks).sum();
    }

    private Stock createStock(User user, Vacation vacation, Integer amount) {
        return Stock.builder()
            .user(user)
            .vacation(vacation)
            .price(vacation.getStock().getPrice().intValue())
            .amount(amount)
            .build();
    }

    private List<ContestParticipation> findAllContestParticipationByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select * from contest_participation as c where c.cahoots_id = ?",
            new BeanPropertyRowMapper<>(ContestParticipation.class),
            vacationId
        );
    }
}
