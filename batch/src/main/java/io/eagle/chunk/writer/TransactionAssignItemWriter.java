package io.eagle.chunk.writer;

import io.eagle.entity.Transaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

public class TransactionAssignItemWriter implements ItemWriter<List<Transaction>> {

    private JpaItemWriter<Transaction> jpaItemWriter;

    public TransactionAssignItemWriter(JpaItemWriter<Transaction> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<Transaction>> items) throws Exception {
        for (List<Transaction> transactions : items) {
            jpaItemWriter.write(transactions);
        }
    }

}
