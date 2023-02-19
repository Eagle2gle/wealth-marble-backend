package io.eagle.chunk.writer;

import io.eagle.entity.Order;
import io.eagle.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class CreateStockItemWriter implements ItemWriter<List<Stock>> {

    private final JpaItemWriter<Stock> jpaItemWriter;

    @Override
    public void write(List<? extends List<Stock>> items) throws Exception {
        for (List<Stock> stocks : items) {
            jpaItemWriter.write(stocks);
        }
    }

}
