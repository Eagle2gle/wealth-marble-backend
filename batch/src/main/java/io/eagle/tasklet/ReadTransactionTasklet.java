package io.eagle.tasklet;

import io.eagle.domain.RecentTransactionRequestVO;
import io.eagle.entity.Transaction;
import io.eagle.service.RecentTransactionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ReadTransactionTasklet implements Tasklet {

    private JdbcTemplate jdbcTemplate;
    private RecentTransactionApiService apiService;

    public ReadTransactionTasklet(DataSource dataSource, RecentTransactionApiService apiService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.apiService = apiService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        RecentTransactionRequestVO vo = this.getRecentlyTransaction();
        Boolean apiResult = this.apiService.service(vo);
        if (apiResult) {
            log.info("API 통신 성공");
        } else {
            log.info("API 통신 실패하거나 거래가 갱신된 데이터가 없습니다.");
        }
        return RepeatStatus.FINISHED;
    }

    private RecentTransactionRequestVO getRecentlyTransaction() {
        List<RecentTransactionRequestVO> vos = jdbcTemplate.query(
            "SELECT t.vacation_id as vacationId, t.price as currentPrice, t.created_at as createdAt FROM transaction t WHERE created_at > DATE_SUB(NOW(), INTERVAL 30 MINUTE) ORDER BY id DESC LIMIT 1",
            new RowMapper<RecentTransactionRequestVO>() {
                @Override
                public RecentTransactionRequestVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new RecentTransactionRequestVO(
                        rs.getLong("vacationId"),
                        rs.getInt("currentPrice"),
                        rs.getString("createdAt")
                    );
                }
            }
        );

        return vos != null && vos.size() > 0 ? vos.get(0) : null;
    }
}
