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
package com.martin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDeletingTasklet implements Tasklet {
    private static final Logger log = LoggerFactory.getLogger(FileDeletingTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String outputFile  = (String) chunkContext.getStepContext().getJobExecutionContext().get("output.file");
        Files.deleteIfExists(Paths.get(outputFile));
        log.debug("deleted {}", outputFile);

        return RepeatStatus.FINISHED;
    }
}
