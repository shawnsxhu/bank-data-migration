package com.pilot.destserver.config;

import com.pilot.destserver.dto.AccountDTO;
import com.pilot.destserver.entity.Account;
import com.pilot.destserver.processor.AccountProcessor;
import com.pilot.destserver.repository.AccountRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class DestinationMigrationConfig {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private KafkaProperties properties;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountProcessor accountProcessor;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DbItemWriter dbItemWriter;


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(15);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("Thread N-> :");
        return executor;
    }

    @Bean
    public AsyncItemProcessor<AccountDTO, Account> accountDTOAsyncItemProcessor(){
        AsyncItemProcessor<AccountDTO, Account> accountDTOAsyncItemProcessor = new AsyncItemProcessor<>();
        accountDTOAsyncItemProcessor.setDelegate(accountProcessor);
        accountDTOAsyncItemProcessor.setTaskExecutor(taskExecutor());
        return accountDTOAsyncItemProcessor;
    }

    @Bean
    public KafkaItemReader<String, AccountDTO> accountDTOKafkaItemReader() {
        Properties props = new Properties();
        props.putAll(this.properties.buildConsumerProperties());

        return new KafkaItemReaderBuilder<String, AccountDTO>()
                .partitions(0)
                .consumerProperties(props)
                .name("account-reader")
                .saveState(true)
                .topic("migration")
                .build();
    }

    @Bean
    public Step dataMigrationStep(ItemReader<AccountDTO> reader, PlatformTransactionManager transactionManager, ItemProcessor<AccountDTO, Account> processor) {
        return new StepBuilder("dataMigrationStep", jobRepository)
                .<AccountDTO, Account>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(dbItemWriter)
                .build();
    }

    @Bean
    public Job dataMigrationJob(Step dataMigrationStep) {
        return new JobBuilder("dataMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dataMigrationStep)
                .build();
    }

    @Bean(name = "launcher")
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
