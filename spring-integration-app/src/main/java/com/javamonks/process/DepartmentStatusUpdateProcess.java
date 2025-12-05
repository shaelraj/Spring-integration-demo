package com.javamonks.process;

import com.javamonks.entity.Department;
import com.javamonks.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DepartmentStatusUpdateProcess implements IntegrationProcess<Department> {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentStatusUpdateProcess(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Message<Department> doProcess(Message<?> message) {
        Department dept = (Department) message.getPayload();
        dept.setStatus("APPROVED");
        return MessageBuilder.withPayload(departmentService.updateDepartment(dept, dept.getDepartmentId()))
                .copyHeaders(message.getHeaders())
                .build();
    }
}
