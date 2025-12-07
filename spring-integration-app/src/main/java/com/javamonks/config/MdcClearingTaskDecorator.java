package com.javamonks.config;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MdcClearingTaskDecorator implements TaskDecorator {
    private static final Logger logger = LoggerFactory.getLogger(MdcClearingTaskDecorator.class);

    @Override
    public Runnable decorate(Runnable runnable) {
        // Capture the MDC context map from the current (parent) thread
        Map<String, String> currentContextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // Set the captured context map onto the new (child) thread
                if (currentContextMap != null) {
                    MDC.setContextMap(currentContextMap);
                }
                // Execute the actual task (e.g., message handling logic)
                runnable.run();
            } finally {
                // Always clear the MDC when the task finishes to prevent leakage
                MDC.clear();
            }
        };
    }
}
