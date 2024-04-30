package com.pilot.srcserver.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MigrationJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("---------------> Starting migration process");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("-----------------> Data migration complete");
    }
}
