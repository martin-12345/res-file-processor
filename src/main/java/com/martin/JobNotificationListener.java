package com.martin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JobNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobNotificationListener.class);

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED !!!");
		}
		taskExecutor.shutdown();
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("!!! JOB STARTING !!!");

		String inputFilename =jobExecution.getJobParameters().getString("input.file.name");
		String outputDir =jobExecution.getJobParameters().getString("output.dir");
		Resource inputResource = new FileSystemResource(inputFilename);
		String outputFile=outputDir+File.separator+inputResource.getFilename().replaceAll("\\.csv$", "-out.csv");
		jobExecution.getExecutionContext().putString("output.file", outputFile);
	}
}