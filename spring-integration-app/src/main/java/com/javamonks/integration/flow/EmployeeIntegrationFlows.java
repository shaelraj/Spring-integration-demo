package com.javamonks.integration.flow;

import com.javamonks.model.Employee;
import com.javamonks.process.DepartmentCodeUpdateProcess;
import com.javamonks.process.DepartmentProcess;
import com.javamonks.process.DepartmentStatusUpdateProcess;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;

@Configuration
public class EmployeeIntegrationFlows {

    public static final Logger LOG = LoggerFactory.getLogger(EmployeeIntegrationFlows.class);


    private final DepartmentProcess departmentProcess;

    private final DepartmentStatusUpdateProcess departmentStatusUpdateProcess;

    private final DepartmentCodeUpdateProcess departmentCodeUpdateProcess;

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public EmployeeIntegrationFlows(DepartmentProcess departmentProcess, DepartmentStatusUpdateProcess departmentStatusUpdateProcess, DepartmentCodeUpdateProcess departmentCodeUpdateProcess, ThreadPoolTaskExecutor taskExecutor) {
        this.departmentProcess = departmentProcess;
        this.departmentStatusUpdateProcess = departmentStatusUpdateProcess;
        this.departmentCodeUpdateProcess = departmentCodeUpdateProcess;
        this.taskExecutor = taskExecutor;
    }


    // #########################  TRANSFORMER  ########################//
    @Bean
    public IntegrationFlow empStatusFlow() {
        return f -> f
                .transform(Object::toString); // simple transformer
    }

    // #########################  SPLITTER  ########################//
    @Bean
    public IntegrationFlow empManagersFlow() {
        return f -> f
                .split(String.class, p -> Arrays.asList(p.toString().split(",")));
    }


    // #########################  FILTER  ########################//
    @Bean
    public IntegrationFlow devEmpFlow() {
        return f -> f
                .filter(p -> p.toString().equalsIgnoreCase("Developer"));
    }

    // #########################  ROUTER  ########################//
    @Bean
    public IntegrationFlow empDepartmentFlow() {
        return f -> f
                .<Employee, String>route(payload -> ((Employee) payload).getDepartment(), mapping -> mapping
                        .subFlowMapping("Engineering", sf -> sf.channel("engineeringChannel"))
                        .subFlowMapping("HR", sf -> sf.channel("hrChannel"))
                        .subFlowMapping("Finance", sf -> sf.channel("financeChannel"))
                );
    }

    // #########################  SIMPLE FLOWS ########################//
    @Bean
    public IntegrationFlow empNameFlow() {
        return f -> f
                .handle((payload, headers) -> "Employee Name: " + payload);
    }

    @Bean
    public IntegrationFlow hireEmpFlow() {
        return f -> f
                .handle((payload, headers) -> {
                    Employee emp = (Employee) payload;
                    emp.setStatus("Hired");
                    return emp;
                });
    }

    @Bean
    @Transactional
    public IntegrationFlow getPendingDepartment() {
        return IntegrationFlow.fromSupplier(() -> departmentProcess.doProcess(MessageBuilder.withPayload("PENDING").build()),
                        e -> e.poller(Pollers.fixedRate(60000)))
//                .channel("departmentChannel")
                .split(Message.class, m -> m.getPayload())
                .handle(departmentStatusUpdateProcess, "doProcess") // marking in progress
                .channel(c -> c.executor(taskExecutor))
                .enrichHeaders(h -> {
                    h.header(MessageHeaders.ERROR_CHANNEL, "errorHandlerFlow");
                })
                .handle(departmentStatusUpdateProcess, "doProcess", e -> e.transactional(true))
                .handle(departmentCodeUpdateProcess, "doProcess")
                .handle(message -> {
                    LOG.info("Completed successfully!!");
                })
                .get();
    }
}
