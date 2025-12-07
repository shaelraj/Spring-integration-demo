package com.javamonks.integration.flow;

import com.javamonks.config.MdcClearingTaskDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class CommonIntegrationFlow {

    private final MdcClearingTaskDecorator taskDecorator;

    @Autowired
    public CommonIntegrationFlow(MdcClearingTaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores); // Configure 5 core threads
        executor.setMaxPoolSize(cores*2);   // Configure 5 max threads
        executor.setQueueCapacity(cores*10); // Optional: No queue, tasks will be rejected if all threads are busy
        executor.setThreadNamePrefix("my-executor-thread-");
        executor.setTaskDecorator(taskDecorator);
        executor.initialize();
        return executor;
    }
   /* @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // Set your desired custom thread name prefix
        scheduler.setThreadNamePrefix("my-integration-poller-");

        // Configure the pool size (default is 1 in Spring Boot, 10 in standard SI)
        // If you have many polling endpoints, increase this number to avoid starvation
        scheduler.setPoolSize(5);

        // Initialize the scheduler
        scheduler.initialize();

        return scheduler;
    }*/
}
