package com.javamonks.integration.flow;

import com.javamonks.entity.Department;
import com.javamonks.model.Employee;
import com.javamonks.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.Message;

import java.util.Arrays;

@Configuration
public class EmployeeIntegrationFlows {

    public static final Logger LOG = LoggerFactory.getLogger(EmployeeIntegrationFlows.class);

    private DepartmentService departmentService;

    @Autowired
    public EmployeeIntegrationFlows(DepartmentService departmentService) {
        this.departmentService = departmentService;
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
    public IntegrationFlow getPendingDepartment() {
        return IntegrationFlow.fromSupplier(departmentService::fetchDepartmentList, e -> e.poller(Pollers.fixedRate(60000)))
//                .channel("departmentChannel")
                .split(Message.class, m -> m.getPayload())
                .handle(message -> {
                    Department data = (Department) message.getPayload();
                    LOG.info("Received data: " + data);
                    // Process the received list as needed
                })
                .get();
    }
}
