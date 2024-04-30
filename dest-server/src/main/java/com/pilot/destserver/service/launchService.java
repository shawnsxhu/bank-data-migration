package com.pilot.destserver.service;

import com.pilot.destserver.mapper.AccountInfoMapper;
import com.pilot.destserver.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class launchService implements CommandLineRunner{
    @Autowired
    @Qualifier("jobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    private Job dataMigrationJob;

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private AccountRepository accountRepository;

    public void launch() {
        JobParameters jobParameters = new JobParameters();
        try {
            jobLauncher.run(dataMigrationJob, jobParameters);
        } catch (Exception e) {
            log.info("-----------------> " + e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        launch();
    }

//    @KafkaListener(topics = "migration", groupId = "1")
//    public void listenAndMigrate(AccountDTO message) {
//        Account account = accountInfoMapper.mapToAccount(message);
//        accountRepository.save(account);
//    }
}
