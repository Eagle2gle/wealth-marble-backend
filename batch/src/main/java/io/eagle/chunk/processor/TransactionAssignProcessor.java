package io.eagle.chunk.processor;

import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionAssignProcessor implements ItemProcessor<Vacation, List<Transaction>> {

    private JdbcTemplate jdbcTemplate;

    public TransactionAssignProcessor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transaction> process(Vacation item) throws Exception {
        List<Order> orders = this.findAllOrderByVacation(item.getId());
        Integer total = this.totalAmount(orders);
        List<Transaction> transactions = new ArrayList<>();
        for(Order order: orders) {
            if (total <= 0) {
                transactions.add(this.createTransaction(item, order, 0));
            } else {
                Double amount = Math.ceil(order.getAmount() * 100 / total);
                total -= amount.intValue();
                if (total <= amount.intValue()) {
                    transactions.add(this.createTransaction(item, order, total));
                    total = 0;
                } else  {
                    transactions.add(this.createTransaction(item, order, amount.intValue()));
                }
            }

        }
        return transactions;
    }

    private Integer totalAmount(List<Order> orders) {
        return orders.stream().mapToInt(Order::getAmount).sum();
    }

    private Transaction createTransaction(Vacation vacation, Order buyOrder, Integer amount) {
        return Transaction.builder()
            .vacation(vacation)
            .buyOrder(buyOrder)
            .price(vacation.getStock().getPrice().intValue())
            .amount(amount)
            .build();
    }

    private List<Order> findAllOrderByVacation(Long vacationId) {
        return jdbcTemplate.query(
            "select * from orders where vacation_id = ? order by amount desc",
            new BeanPropertyRowMapper<>(Order.class),
            vacationId
        );
    }
}
