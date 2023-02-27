package io.eagle.job;

import io.eagle.listener.CustomJobExecutionListener;
import io.eagle.listener.CustomStepExecutionListener;
import io.eagle.service.RecentTransactionApiService;
import io.eagle.tasklet.ReadTransactionTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static io.eagle.common.BatchConstant.*;

@Configuration
@RequiredArgsConstructor
public class RecentTransactionJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job recentJob() {
        return jobBuilderFactory.get(RECENT_TRANSACTION_JOB)
            .incrementer(new RunIdIncrementer())
            .listener(new CustomJobExecutionListener())
            .start(recentStep())
            .build();
    }

    @Bean
    @JobScope
    public Step recentStep() {

        return stepBuilderFactory.get(RECENT_TRANSACTION_STEP)
            .listener(new CustomStepExecutionListener())
            .tasklet(new ReadTransactionTasklet(dataSource, new RecentTransactionApiService()))
            .build();
    }

}
