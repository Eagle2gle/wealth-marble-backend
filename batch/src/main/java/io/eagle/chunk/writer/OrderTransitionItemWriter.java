package io.eagle.chunk.writer;

import io.eagle.entity.Order;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

public class OrderTransitionItemWriter implements ItemWriter<List<Order>> {

    private JpaItemWriter<Order> jpaItemWriter;

    public OrderTransitionItemWriter(JpaItemWriter<Order> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<Order>> items) throws Exception {
        for (List<Order> orders : items) {
            jpaItemWriter.write(orders);
        }
    }

}
