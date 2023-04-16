package io.eagle.chunk.processor;

import io.eagle.domain.ContestParticipationVO;
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
        List<ContestParticipationVO> contestParticipations = this.findAllContestParticipationByVacation(item.getId());
        Integer total = this.totalAmount(contestParticipations);
        return this.assignStocks(contestParticipations, total, item);
    }

    private List<Stock> assignStocks(List<ContestParticipationVO> contestParticipations, Integer total, Vacation item) {
        List<Stock> stocks = new ArrayList<>();
        for (ContestParticipationVO contestParticipation: contestParticipations) {
            Long userId = contestParticipation.getUserId();
            User user = findUserById(userId);
            if (total > 0) {
                Integer amount = Math.round(contestParticipation.getStocks() * item.getStock().getNum() / total);
                if (total <= amount) {
                    stocks.add(this.createStock(user, item, total));
                    total = 0;
                } else {
                    stocks.add(this.createStock(user, item, amount.intValue()));
                    total -= amount;
                }
            }
        }
        return stocks;
    }

    private Integer totalAmount(List<ContestParticipationVO> contestParticipations) {
        return contestParticipations.stream().mapToInt(ContestParticipationVO::getStocks).sum();
    }

    private Stock createStock(User user, Vacation vacation, Integer amount) {
        return Stock.builder()
            .user(user)
            .vacation(vacation)
            .price(vacation.getStock().getPrice().intValue())
            .amount(amount)
            .build();
    }

    private List<ContestParticipationVO> findAllContestParticipationByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select c.id, c.user_id as userId, c.cahoots_id as vacationId, c.stocks, c.status" +
                " from contest_participation as c where c.cahoots_id = ?",
            new BeanPropertyRowMapper<>(ContestParticipationVO.class),
            vacationId
        );
    }

    private User findUserById(Long userId) {
        List<User> users = jdbcTemplate.query(
            "select * from user as u where u.id = ?",
            new BeanPropertyRowMapper<>(User.class),
            userId
        );

        if (users == null || users.size() < 1) {
            return null;
        }
        return users.get(0);
    }
}
