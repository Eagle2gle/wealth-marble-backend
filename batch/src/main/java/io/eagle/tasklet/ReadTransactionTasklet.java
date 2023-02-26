package io.eagle.tasklet;

import io.eagle.entity.Transaction;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ReadTransactionTasklet implements Tasklet {

    private JdbcTemplate jdbcTemplate;

    public ReadTransactionTasklet(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return RepeatStatus.FINISHED;
    }

    private List<Transaction> getRecentlyTransaction() {
        return jdbcTemplate.query(
            "SELECT TOP 1 * FROM transaction ORDER BY id DESC",
            new BeanPropertyRowMapper(Transaction.class)
        );
    }
}
