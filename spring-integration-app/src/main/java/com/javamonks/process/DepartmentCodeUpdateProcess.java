package com.javamonks.process;

import com.javamonks.entity.Department;
import com.javamonks.services.DepartmentService;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DepartmentCodeUpdateProcess implements IntegrationProcess<Department> {

    private final DepartmentService departmentService;

    public DepartmentCodeUpdateProcess(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Message<Department> doProcess(Message<?> message) {
        Department dept = (Department) message.getPayload();
        dept.setCode("007");
        return MessageBuilder.withPayload(departmentService.updateDepartment(dept, dept.getDepartmentId()))
                .copyHeaders(message.getHeaders())
                .build();
    }
}
