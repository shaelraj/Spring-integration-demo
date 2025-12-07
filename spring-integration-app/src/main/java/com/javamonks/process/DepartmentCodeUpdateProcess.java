package com.javamonks.process;

import com.javamonks.entity.Department;
import com.javamonks.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DepartmentCodeUpdateProcess implements IntegrationProcess {

    public static final Logger LOG = LoggerFactory.getLogger(DepartmentCodeUpdateProcess.class);

    private final DepartmentService departmentService;

    public DepartmentCodeUpdateProcess(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Message<?> doProcess(Message<?> message) {
        Department dept = (Department) message.getPayload();
        LOG.info("Department code process Start!!!");
        dept.setCode("007");
        if(dept.getDepartmentId() == 2){
            throw new RuntimeException("Dept is is :" + dept.getDepartmentId());
        }

        LOG.info("Department code process End!!!");
        return MessageBuilder.withPayload(departmentService.updateDepartment(dept, dept.getDepartmentId()))
                .copyHeaders(message.getHeaders())
                .build();
    }
}
