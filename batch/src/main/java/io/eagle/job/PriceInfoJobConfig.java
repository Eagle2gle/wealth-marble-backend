package io.eagle.job;

import io.eagle.entity.PriceInfo;
import io.eagle.listener.CustomJobExecutionListener;
import io.eagle.listener.CustomStepExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;

import static io.eagle.common.BatchConstant.*;
import static io.eagle.common.BatchConstant.TRANSACTIONS_SUMMARY_STEP;
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
                .start(vacationTransitionStep())
                .build();
    }

    @Bean
    @JobScope
    public Step vacationTransitionStep() {
        return stepBuilderFactory.get(TRANSACTIONS_SUMMARY_STEP)
                .<PriceInfo, PriceInfo>chunk(CHUNK_SIZE)
                .reader(transactionsSummaryItemReader())
                .writer(transactionsSummaryItemWriter())
                .listener(new CustomStepExecutionListener())
                .build();
    }

    @Bean(TRANSACTIONS_SUMMARY_STEP + "_reader")
    @StepScope
    public JpaCursorItemReader<PriceInfo> transactionsSummaryItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("lastday", LocalDate.now().minusDays(1L));
        parameters.put("twoDaysAgo", LocalDate.now().minusDays(2L));

        return new JpaCursorItemReaderBuilder<PriceInfo>()
                .entityManagerFactory(entityManagerFactory)
                .queryString(TransactionSummaryQuery.getQuery())
                .parameterValues(parameters)
                .name(TRANSACTIONS_SUMMARY_READER)
                .build();
    }

    @Bean(TRANSACTIONS_SUMMARY_STEP + "_writer")
    @StepScope
    public JpaItemWriter<PriceInfo> transactionsSummaryItemWriter() {
        return new JpaItemWriterBuilder<PriceInfo>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
