package io.eagle.chunk.writer;

import io.eagle.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class OrderTransitionItemWriter implements ItemWriter<List<Order>> {

    private final JpaItemWriter<Order> jpaItemWriter;

    @Override
    public void write(List<? extends List<Order>> items) throws Exception {
        for (List<Order> orders : items) {
            jpaItemWriter.write(orders);
        }
    }

}
