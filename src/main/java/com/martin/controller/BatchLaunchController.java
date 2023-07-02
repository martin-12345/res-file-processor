/*
 *   Copyright (c) 2023 Martin Newstead.  All Rights Reserved.
 *
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 */
package com.martin.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class BatchLaunchController {

    private final JobLauncher jobLauncher;
    private final Job salvageJob;

    public BatchLaunchController(JobLauncher jobLauncher, Job salvageJob) {
        this.jobLauncher = jobLauncher;
        this.salvageJob = salvageJob;
    }

    @PostMapping("/salvage")
    public ResponseEntity<ResponseMessage> launchBatch(@RequestBody LaunchParms launchParams) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        String inputFile = launchParams.getInputFile();
        String outputDir = launchParams.getOutputDir();

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("run.date", new Date())
                .addString("input.file.name",inputFile)
                .addString("output.dir", outputDir)
                .toJobParameters();
        final JobExecution jobExecution = jobLauncher.run(salvageJob, jobParameters);
        ExitStatus exit = jobExecution.getExitStatus();

        if(!exit.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
            return new ResponseEntity<>(new ResponseMessage(exit.getExitCode(), exit.getExitCode()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Integer lines;
        if((lines = (Integer)jobExecution.getExecutionContext().get("line.count")) != null) {
            return new ResponseEntity<>(new ResponseMessage(exit.getExitCode(), lines + " lines"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponseMessage(exit.getExitCode(), "Unknown lines"), HttpStatus.OK);
    }
}
