package com.javamonks.integration.flow;

import com.javamonks.model.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.Arrays;

@Configuration
public class EmployeeIntegrationFlows {

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
                .split(String.class,p -> Arrays.asList(p.toString().split(",")));
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
}
