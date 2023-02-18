package io.eagle.job;

import io.eagle.chunk.processor.CreateStockProcessor;
import io.eagle.chunk.processor.VacationTransitionProcessor;
import io.eagle.chunk.writer.CreateStockItemWriter;
import io.eagle.entity.Stock;
import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
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
import org.springframework.batch.item.ItemProcessor;
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
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class VacationJobConfiguration {

    public static final String VACATION_JOB = "vacationTransitionJob";
    public static final String VACATION_TRANSITION_STEP = "vacationTransitionStep";
    public static final String CREATE_STOCK = "createStock";
    public static final String EXPIRED_VACATION_PAGE_READER = "vacationJpaPagingItemReader";
    public static final String CREATE_STOCK_READER = "createStockReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 10;

    @Bean
    public Job vacationJob() {
        return jobBuilderFactory.get(VACATION_JOB)
            .incrementer(new RunIdIncrementer())
            .listener(new CustomJobExecutionListener())
            .start(vacationTransitionStep())
            .next(orderTransitionStep())
            .build();
    }

    @Bean
    @JobScope
    public Step vacationTransitionStep() {
        return stepBuilderFactory.get(VACATION_TRANSITION_STEP)
            .<Vacation, Vacation>chunk(chunkSize)
            .reader(vacationTransitionItemReader())
            .processor(vacationTransitionItemProcessor())
            .writer(vacationTransitionItemWriter())
            .listener(new CustomStepExecutionListener())
            .build();
    }

    @Bean(VACATION_TRANSITION_STEP + "_reader")
    @StepScope
    public JpaCursorItemReader<Vacation> vacationTransitionItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("end", LocalDate.now().minusDays(1L));
        parameters.put("status", VacationStatusType.CAHOOTS_ONGOING);
        return new JpaCursorItemReaderBuilder<Vacation>()
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT v FROM Vacation v WHERE status = :status and stockPeriod.end = :end")
            .parameterValues(parameters)
            .name(EXPIRED_VACATION_PAGE_READER)
            .build();
    }

    @Bean(VACATION_TRANSITION_STEP + "_processor")
    @StepScope
    public ItemProcessor<Vacation, Vacation> vacationTransitionItemProcessor() {
        return new VacationTransitionProcessor(dataSource);
    }

    @Bean(VACATION_TRANSITION_STEP + "_writer")
    @StepScope
    public JpaItemWriter<Vacation> vacationTransitionItemWriter() {
        return new JpaItemWriterBuilder<Vacation>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

    @Bean
    @JobScope
    public Step orderTransitionStep() {
        return stepBuilderFactory.get(CREATE_STOCK + "_step")
            .<Vacation, List<Stock>>chunk(chunkSize)
            .reader(createStockItemReader())
            .processor(createStockItemProcessor())
            .writer(orderTransitionItemWriter())
            .listener(new CustomStepExecutionListener())
            .build();
    }

    @Bean(CREATE_STOCK + "_reader")
    @StepScope
    public JpaCursorItemReader<Vacation> createStockItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("end", LocalDate.now().minusDays(1L));
        return new JpaCursorItemReaderBuilder<Vacation>()
            .queryString("SELECT v FROM Vacation v WHERE stockPeriod.end = :end")
            .entityManagerFactory(entityManagerFactory)
            .parameterValues(parameters)
            .name(CREATE_STOCK_READER)
            .build();
    }

    @Bean(CREATE_STOCK + "_processor")
    @StepScope
    public ItemProcessor<Vacation, List<Stock>> createStockItemProcessor() {
        return new CreateStockProcessor(dataSource);
    }

    @Bean(CREATE_STOCK + "_writer")
    @StepScope
    public CreateStockItemWriter orderTransitionItemWriter() {
        JpaItemWriter<Stock> jpaItemWriter = new JpaItemWriter();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return new CreateStockItemWriter(jpaItemWriter);
    }

}
