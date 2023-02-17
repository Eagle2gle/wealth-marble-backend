package io.eagle.job;

import io.eagle.entity.Vacation;
import io.eagle.listener.CustomJobExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class VacationJobConfiguration {

    public static final String VACATION_JOB = "vacationTransitionJob";
    public static final String VACATION_TRANSITION_STEP = "vacationTransitionStep";
    public static final String EXPIRED_VACATION_PAGE_READER = "vacationJpaPagingItemReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    private final int chunkSize = 10;

    @Bean
    public Job vacationJob() {
        return jobBuilderFactory.get(VACATION_JOB)
            .incrementer(new RunIdIncrementer())
            .listener(new CustomJobExecutionListener())
            .start(vacationTransitionStep())
            .build();
    }

    @Bean(VACATION_JOB)
    @JobScope
    public Step vacationTransitionStep() {
        return stepBuilderFactory.get(VACATION_TRANSITION_STEP)
            .<Vacation, Vacation>chunk(chunkSize)
            .reader(vacationPagingItemReader())
            .build();
    }

    @Bean(VACATION_TRANSITION_STEP)
    @StepScope
    public JdbcCursorItemReader<Vacation> vacationPagingItemReader() {
        return new JdbcCursorItemReaderBuilder<Vacation>()
            .sql("SELECT v FROM Vacation v WHERE DATE(v.stockPeriod.end) = CURDATE() and v.status = io.eagle.entity.type.VacationStatusType.CAHOOTS_ONGOING")
            .rowMapper(new BeanPropertyRowMapper<>(Vacation.class))
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .name(EXPIRED_VACATION_PAGE_READER)
            .build();
    }

}
