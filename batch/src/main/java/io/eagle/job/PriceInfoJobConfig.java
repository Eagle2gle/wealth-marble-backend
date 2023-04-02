package io.eagle.job;

import io.eagle.chunk.processor.CopyPriceInfoProcessor;
import io.eagle.chunk.processor.PriceInfoProcessor;
import io.eagle.domain.PriceInfoVO;
import io.eagle.entity.PriceInfo;
import io.eagle.listener.CustomJobExecutionListener;
import io.eagle.listener.CustomStepExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.support.ListPreparedStatementSetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static io.eagle.common.BatchConstant.*;
import static io.eagle.common.BatchConstant.TRANSACTIONS_SUMMARY_STEP;
import static io.eagle.common.ReaderQuery.SelectNoTransactionsQuery;
import static io.eagle.common.ReaderQuery.TransactionSummaryQuery;

@Configuration
@RequiredArgsConstructor
public class PriceInfoJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job priceInfoJob() {
        return jobBuilderFactory.get(PRICE_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(new CustomJobExecutionListener())
                .start(transactionsSummaryStep())
                .next(transferLastdayPriceInfoStep())
                .build();
    }

    @Bean
    @JobScope
    public Step transactionsSummaryStep() {
        return stepBuilderFactory.get(TRANSACTIONS_SUMMARY_STEP)
                .<PriceInfoVO, PriceInfo>chunk(CHUNK_SIZE)
                .reader(transactionsSummaryItemReader())
                .processor(new PriceInfoProcessor())
                .writer(transactionsSummaryItemWriter())
                .listener(new CustomStepExecutionListener())
                .build();
    }

    @Bean(TRANSACTIONS_SUMMARY_STEP + "_reader")
    @StepScope
    public JdbcCursorItemReader<PriceInfoVO> transactionsSummaryItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        LocalDate today = LocalDate.now(); // LocalDate.of(2023,02,11); // for test
        parameters.put("lastday", today.minusDays(1L));
        parameters.put("twoDaysAgo", today.minusDays(2L));

        return new JdbcCursorItemReaderBuilder<PriceInfoVO>()
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .name(TRANSACTIONS_SUMMARY_READER)
                .sql(NamedParameterUtils.substituteNamedParameters(TransactionSummaryQuery.getQuery(), new MapSqlParameterSource(parameters)))
                .preparedStatementSetter(new ListPreparedStatementSetter(Arrays.asList(NamedParameterUtils.buildValueArray(TransactionSummaryQuery.getQuery(), parameters))))
                .beanRowMapper(PriceInfoVO.class)
                .build();
    }

    @Bean(TRANSACTIONS_SUMMARY_STEP + "_writer")
    @StepScope
    public JpaItemWriter<PriceInfo> transactionsSummaryItemWriter() {
        return new JpaItemWriterBuilder<PriceInfo>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    @JobScope
    public Step transferLastdayPriceInfoStep() {
        return stepBuilderFactory.get(TRANSFER_LASTDAY_PRICEINFO_STEP)
                .<PriceInfoVO, PriceInfo>chunk(CHUNK_SIZE)
                .reader(transferLastdayPriceInfoItemReader())
                .processor(new CopyPriceInfoProcessor())
                .writer(transferLastdayPriceInfoItemWriter())
                .listener(new CustomStepExecutionListener())
                .build();
    }

    @Bean(TRANSFER_LASTDAY_PRICEINFO_STEP + "_reader")
    @StepScope
    public JdbcCursorItemReader<PriceInfoVO> transferLastdayPriceInfoItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        LocalDate today = LocalDate.now(); // LocalDate.of(2023,02,11); // for test
        parameters.put("lastday", today.minusDays(1L));
        parameters.put("twoDaysAgo", today.minusDays(2L));

        return new JdbcCursorItemReaderBuilder<PriceInfoVO>()
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .name(TRANSFER_LASTDAY_PRICEINFO_READER)
                .sql(NamedParameterUtils.substituteNamedParameters(SelectNoTransactionsQuery.getQuery(), new MapSqlParameterSource(parameters)))
                .preparedStatementSetter(new ListPreparedStatementSetter(Arrays.asList(NamedParameterUtils.buildValueArray(SelectNoTransactionsQuery.getQuery(), parameters))))
                .beanRowMapper(PriceInfoVO.class)
                .build();
    }


    @Bean(TRANSFER_LASTDAY_PRICEINFO_STEP + "_writer")
    @StepScope
    public JpaItemWriter<PriceInfo> transferLastdayPriceInfoItemWriter() {
        return new JpaItemWriterBuilder<PriceInfo>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
