package com.javamonks.integration.flow;

import com.javamonks.entity.Department;
import com.javamonks.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ErrorIntegrationFlow {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Bean
    public IntegrationFlow errorHandlerWorkFlow() {
        return IntegrationFlow.from("errorHandlerFlow")
                .handle((payload, headers) -> {
                    // The 'payload' of the ErrorMessage is the Exception
                    Throwable cause = (Throwable) payload;

                    // The original message is available as 'failedMessage' within the exception
                    if (cause instanceof MessageHandlingException) {
                        Message<?> failedMessage = ((MessageHandlingException) cause).getFailedMessage();
                        Object originalPayload = failedMessage.getPayload();
                        MessageHeaders originalHeaders = failedMessage.getHeaders();

                        System.err.println("Original Payload: " + originalPayload);
                        System.err.println("Original Headers: " + originalHeaders);

                        if(originalPayload instanceof Department){
                            Department department = (Department) originalPayload;
                           Optional<Department> dept = departmentRepository.findById(department.getDepartmentId());
                            dept.get().setStatus("FAILED");
                            departmentRepository.save(dept.get());

                        }
                    }

                    // Perform error logging, notifications, or other error handling logic
                    return null; // Or return a new message if needed
                })
                .get();
    }

}
