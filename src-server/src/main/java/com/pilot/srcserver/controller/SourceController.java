package com.pilot.srcserver.controller;

import com.pilot.srcserver.service.ScheduledSendService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send/v1")
public class SourceController {
    @Autowired
    private ScheduledSendService scheduledSendService;

    @PostMapping("batch")
    public ResponseEntity<?> sendMigration(){
        try {
            scheduledSendService.scheduledSend();
        } catch (JobInstanceAlreadyCompleteException | JobRestartException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
