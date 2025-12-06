/*
package com.javamonks.config;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Service;

@Service
public class MdcClearingTaskDecorator implements TaskDecorator {
    private static final Logger logger = LoggerFactory.getLogger(MdcClearingTaskDecorator.class);

    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } finally {
                logger.debug("Cleaning the MDC context");
                ThreadContext.clearAll();
            }
        };
    }
}
*/
