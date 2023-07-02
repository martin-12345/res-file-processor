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

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class LineCounter implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context) {

    }

    @Override
    public void afterChunk(ChunkContext context) {
        int count = context.getStepContext().getStepExecution().getReadCount();
        context.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("line.count", count);

    }

    @Override
    public void afterChunkError(ChunkContext context) {

    }
}
