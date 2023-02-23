package io.eagle.job;

import io.eagle.chunk.processor.CreateStockProcessor;
import io.eagle.chunk.processor.RetrieveMoneyProcessor;
import io.eagle.chunk.processor.VacationTransitionProcessor;
import io.eagle.chunk.writer.CreateStockItemWriter;
import io.eagle.chunk.writer.DeleteContestParticipationItemWriter;
import io.eagle.chunk.writer.RetrieveMoneyWriter;
import io.eagle.domain.RetrieveMoneyVO;
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
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static io.eagle.common.BatchConstant.*;

@Configuration
@RequiredArgsConstructor
public class VacationJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job vacationJob() {
        return jobBuilderFactory.get(VACATION_JOB)
            .incrementer(new RunIdIncrementer())
            .listener(new CustomJobExecutionListener())
            .start(vacationTransitionStep())
            .next(createStockStep())
            .next(retrieveMoneyStep())
            .build();
    }

    @Bean
    @JobScope
    public Step vacationTransitionStep() {
        return stepBuilderFactory.get(VACATION_TRANSITION_STEP)
            .<Vacation, Vacation>chunk(CHUNK_SIZE)
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
    public Step createStockStep() {
        return stepBuilderFactory.get(CREATE_STOCK + "_step")
            .<Vacation, List<Stock>>chunk(CHUNK_SIZE)
            .reader(createStockItemReader())
            .processor(createStockItemProcessor())
            .writer(createStockItemWriter())
            .listener(new CustomStepExecutionListener())
            .build();
    }

    @Bean(CREATE_STOCK + "_reader")
    @StepScope
    public JpaCursorItemReader<Vacation> createStockItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("end", LocalDate.now().minusDays(1L));
        parameters.put("status", VacationStatusType.MARKET_ONGOING);
        return new JpaCursorItemReaderBuilder<Vacation>()
            .queryString("SELECT v FROM Vacation v WHERE stockPeriod.end = :end and status = :status")
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
    public CreateStockItemWriter createStockItemWriter() {
        JpaItemWriter<Stock> jpaItemWriter = new JpaItemWriter();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return new CreateStockItemWriter(jpaItemWriter);
    }

    @Bean
    @JobScope
    public Step retrieveMoneyStep() {
        return stepBuilderFactory.get(RETRIEVE_MONEY + "_step")
            .listener(new CustomStepExecutionListener())
            .<Vacation, List<RetrieveMoneyVO>>chunk(CHUNK_SIZE)
            .reader(retrieveMoneyItemReader())
            .processor(retrieveMoneyItemProcessor())
            .writer(retrieveMoneyItemWriter())
            .build();
    }

    @Bean(RETRIEVE_MONEY + "_reader")
    @StepScope
    public JpaCursorItemReader<Vacation> retrieveMoneyItemReader() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("end", LocalDate.now().minusDays(1L));
        parameters.put("status", VacationStatusType.CAHOOTS_CLOSE);
        return new JpaCursorItemReaderBuilder<Vacation>()
            .queryString("SELECT v FROM Vacation v WHERE stockPeriod.end = :end and status = :status")
            .entityManagerFactory(entityManagerFactory)
            .parameterValues(parameters)
            .name(RETRIEVE_MONEY + "_reader")
            .build();
    }

    @Bean(RETRIEVE_MONEY + "_processor")
    @StepScope
    public ItemProcessor<Vacation, List<RetrieveMoneyVO>> retrieveMoneyItemProcessor() {
        return new RetrieveMoneyProcessor(dataSource);
    }

    @Bean(RETRIEVE_MONEY + "_writer")
    @StepScope
    public CompositeItemWriter<List<RetrieveMoneyVO>> retrieveMoneyItemWriter() {
        CompositeItemWriter<List<RetrieveMoneyVO>> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(updateCash(), deleteContestParticipation()));
        return compositeItemWriter;
    }

    @Bean(RETRIEVE_MONEY + "_writer_update_cash")
    @StepScope
    public RetrieveMoneyWriter updateCash() {
        JdbcBatchItemWriter jdbcBatchItemWriter = new JdbcBatchItemWriter();
        jdbcBatchItemWriter.setDataSource(dataSource);
        return new RetrieveMoneyWriter(jdbcBatchItemWriter);
    }

    @Bean(RETRIEVE_MONEY + "_writer_delete_contest_participation")
    @StepScope
    public DeleteContestParticipationItemWriter deleteContestParticipation() {
        JdbcBatchItemWriter jdbcBatchItemWriter = new JdbcBatchItemWriter();
        jdbcBatchItemWriter.setDataSource(dataSource);
        return new DeleteContestParticipationItemWriter(jdbcBatchItemWriter);
    }

}
