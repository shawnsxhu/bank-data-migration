package com.pilot.srcserver.config;

import com.pilot.srcserver.dto.AccountDTO;
import com.pilot.srcserver.entity.Account;
import com.pilot.srcserver.processor.AccountDTOProcessor;
import com.pilot.srcserver.repository.AccountRepository;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class SrcToDestMigrationConfig {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    @Qualifier("transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private KafkaTemplate<String, AccountDTO> kafkaTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountDTOProcessor accountDTOProcessor;

    @Autowired
    private DataSource dataSource;


    @Bean
    @SneakyThrows
    public ItemReader<Account> sqlDataReader() {
        // Configure the SQLData reader
        RepositoryItemReader<Account> accountItemReader = new RepositoryItemReader<Account>();
        accountItemReader.setRepository(accountRepository);
        accountItemReader.setMethodName("findAll");
        accountItemReader.setSort(Map.of("accountId", Sort.Direction.ASC));
        accountItemReader.setPageSize(10);
        accountItemReader.afterPropertiesSet();
        return accountItemReader;
    }

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
    public AsyncItemProcessor<Account, AccountDTO> accountDTOAsyncItemProcessor(){
        AsyncItemProcessor<Account, AccountDTO> accountDTOAsyncItemProcessor = new AsyncItemProcessor<>();
        accountDTOAsyncItemProcessor.setDelegate(accountDTOProcessor);
        accountDTOAsyncItemProcessor.setTaskExecutor(taskExecutor());
        return accountDTOAsyncItemProcessor;
    }

    @Bean
    public AsyncItemWriter<AccountDTO> accountDTOAsyncItemWriter(){
        AsyncItemWriter<AccountDTO> asyncWriter = new AsyncItemWriter<>();
        asyncWriter.setDelegate(accountDTOKafkaItemWriter());
        return asyncWriter;
    }

    @Bean
    @SneakyThrows
    public KafkaItemWriter<String, AccountDTO> accountDTOKafkaItemWriter(){
        KafkaItemWriter<String, AccountDTO> kafkaItemWriter = new KafkaItemWriter<>();
        kafkaItemWriter.setKafkaTemplate(kafkaTemplate);
        kafkaItemWriter.setItemKeyMapper(AccountDTO::getAccountNumber);
        kafkaItemWriter.setDelete(Boolean.FALSE);
        kafkaItemWriter.afterPropertiesSet();
        return kafkaItemWriter;
    }

    @Bean
    public Step dataMigrationStep(ItemReader<Account> reader, ItemProcessor<Account, AccountDTO> processor,
                                  ItemWriter<AccountDTO> writer) {
        return new StepBuilder("dataMigrationStep", jobRepository)
                .<Account, AccountDTO>chunk(100, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "launcher")
    public Job dataMigrationJob(Step dataMigrationStep) {
        return new JobBuilder("dataMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(dataMigrationStep)
                .end()
                .build();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
