package io.eagle.chunk.writer;

import io.eagle.domain.RetrieveMoneyVO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import java.util.List;

public class DeleteContestParticipationItemWriter implements ItemWriter<List<RetrieveMoneyVO>> {

    private JdbcBatchItemWriter jdbcBatchItemWriter;

    public DeleteContestParticipationItemWriter(JdbcBatchItemWriter jdbcBatchItemWriter) {
        this.jdbcBatchItemWriter = jdbcBatchItemWriter;
    }

    @Override
    public void write(List<? extends List<RetrieveMoneyVO>> items) throws Exception {
        for (List<RetrieveMoneyVO> retrieveMoneyVOS: items) {
            jdbcBatchItemWriter.setSql("UPDATE contest_participation SET status = :status WHERE id = :contestParticipationId");
            jdbcBatchItemWriter.write(retrieveMoneyVOS);
        }
    }
}
