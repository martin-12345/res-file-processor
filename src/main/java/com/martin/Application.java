package com.martin;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Application /*implements CommandLineRunner*/ {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

/*    @Override
    public void run(String... args) throws Exception {

        // Pass the required Job Parameters from here to read it anywhere within
        // Spring Batch infrastructure
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input.file.name", "/home/martin/Downloads/data-xxx.csv")
                .addString("output.dir", "/tmp")
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
        System.out.println("STATUS :: " + execution.getStatus());
    }*/
}
