package io.eagle.chunk.processor;

import io.eagle.entity.Order;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.OrderStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class OrderFailProcessor implements ItemProcessor<Vacation, List<Order>> {

    private JdbcTemplate jdbcTemplate;

    public OrderFailProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Order> process(Vacation item) throws Exception {
        return findAllOrderByVacation(item.getId()).stream().map(order -> {
            order.setStatus(OrderStatus.FAILED);
            return order;
        }).collect(Collectors.toList());
    }

    private List<Order> findAllOrderByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select * from orders where vacation_id = ?",
            new BeanPropertyRowMapper<>(Order.class),
            vacationId
        );
    }
}
