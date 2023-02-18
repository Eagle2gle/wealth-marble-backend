package io.eagle.chunk.writer;

import io.eagle.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class TransactionAssignItemWriter implements ItemWriter<List<Transaction>> {

    private final JpaItemWriter<Transaction> jpaItemWriter;

    @Override
    public void write(List<? extends List<Transaction>> items) throws Exception {
        for (List<Transaction> transactions : items) {
            jpaItemWriter.write(transactions);
        }
    }

}
