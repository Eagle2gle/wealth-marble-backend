package io.eagle.job;

import io.eagle.chunk.processor.OrderTransitionProcessor;
import io.eagle.chunk.processor.TransactionAssignProcessor;
import io.eagle.chunk.processor.VacationTransitionProcessor;
import io.eagle.chunk.writer.OrderTransitionItemWriter;
import io.eagle.chunk.writer.TransactionAssignItemWriter;
import io.eagle.entity.Order;
import io.eagle.entity.Transaction;
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
    public static final String ORDER_TRANSITION = "orderTransition";
    public static final String TRANSACTION_ASSIGN = "transactionAssign";
    public static final String EXPIRED_VACATION_PAGE_READER = "vacationJpaPagingItemReader";
    public static final String ORDER_TRANSITION_READER = "orderTransitionReader";
    public static final String TRANSACTION_ASSIGN_READER = "transactionAssignReader";

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
            .next(transactionAssignStep())
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
        return stepBuilderFactory.get(ORDER_TRANSITION + "_step")
            .<Vacation, List<Order>>chunk(chunkSize)
            .reader(orderTransitionItemReader())
            .processor(orderTransitionItemProcessor())
            .writer(orderTransitionItemWriter())
            .build();
    }

    @Bean(ORDER_TRANSITION + "_reader")
    @StepScope
    public JpaCursorItemReader<Vacation> orderTransitionItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("end", LocalDate.now().minusDays(1L));
        return new JpaCursorItemReaderBuilder<Vacation>()
            .queryString("SELECT v FROM Vacation v WHERE end = :end")
            .entityManagerFactory(entityManagerFactory)
            .name(ORDER_TRANSITION_READER)
            .build();
    }

    @Bean(ORDER_TRANSITION + "_processor")
    @StepScope
    public ItemProcessor<Vacation, List<Order>> orderTransitionItemProcessor() {
        return new OrderTransitionProcessor(dataSource);
    }

    @Bean(ORDER_TRANSITION + "_writer")
    @StepScope
    public OrderTransitionItemWriter orderTransitionItemWriter() {
        JpaItemWriter<Order> jpaItemWriter = new JpaItemWriter();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return new OrderTransitionItemWriter(jpaItemWriter);
    }

    @Bean
    @JobScope
    public Step transactionAssignStep() {
        return stepBuilderFactory.get(TRANSACTION_ASSIGN + "_step")
            .<Vacation, List<Transaction>>chunk(chunkSize)
            .reader(transactionAssignItemReader())
            .processor(transactionAssignItemProcessor())
            .writer(transactionAssignItemWriter())
            .build();
    }

    @Bean(TRANSACTION_ASSIGN + "_reader")
    @StepScope
    public JpaCursorItemReader<Vacation> transactionAssignItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("status", VacationStatusType.MARKET_ONGOING);
        parameters.put("end", LocalDate.now().minusDays(1L));
        return new JpaCursorItemReaderBuilder<Vacation>()
            .queryString("SELECT v FROM Vacation v WHERE status = :status and end = :end")
            .entityManagerFactory(entityManagerFactory)
            .name(TRANSACTION_ASSIGN_READER)
            .build();
    }

    @Bean(TRANSACTION_ASSIGN + "_processor")
    @StepScope
    public ItemProcessor<Vacation, List<Transaction>> transactionAssignItemProcessor() {
        return new TransactionAssignProcessor(dataSource);
    }

    @Bean(TRANSACTION_ASSIGN + "_writer")
    @StepScope
    public TransactionAssignItemWriter transactionAssignItemWriter() {
        JpaItemWriter<Transaction> jpaItemWriter = new JpaItemWriter();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return new TransactionAssignItemWriter(jpaItemWriter);
    }

}
