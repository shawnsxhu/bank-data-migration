package com.pilot.srcserver.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledSendService implements SourceService{
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job dataMigrationJob;


    @Scheduled(cron = "0 0 22 * * ?")
    public void scheduledSend() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters();
        jobLauncher.run(dataMigrationJob, jobParameters);
    }
}
