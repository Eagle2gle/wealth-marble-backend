package io.eagle.chunk.processor;

import io.eagle.domain.order.vo.OrderHistoryVO;
import io.eagle.entity.Order;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.rowmapper.OrderHistoryVORowMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class VacationTransitionProcessor implements ItemProcessor<Vacation, Vacation> {

    private JdbcTemplate jdbcTemplate;

    public VacationTransitionProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Vacation process(Vacation vacation) throws Exception {
        return this.isParticipationSuccess(vacation);
    }

    private Vacation isParticipationSuccess(Vacation vacation) {
        List<OrderHistoryVO> histories = this.findAllOrderByVacation(vacation.getId());
        Integer totalAmount = this.calculateOrderAmount(histories);

        if (totalAmount >= vacation.getStock().getNum()) {
            vacation.setStatus(VacationStatusType.MARKET_ONGOING);
            return vacation;
        }
        vacation.setStatus(VacationStatusType.CAHOOTS_CLOSE);
        return vacation;
    }

    private Integer calculateOrderAmount(List<OrderHistoryVO> historyVOS) {
        return historyVOS.stream().mapToInt(OrderHistoryVO::getAmount).sum();
    }

    private List<OrderHistoryVO> findAllOrderByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select * from orders where vacation_id = ?",
            new OrderHistoryVORowMapper(),
            vacationId
        );
    }
}
