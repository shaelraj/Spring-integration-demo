package com.javamonks.process;

import com.javamonks.entity.Department;
import com.javamonks.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentProcess implements IntegrationProcess {
    public static final Logger LOG = LoggerFactory.getLogger(DepartmentProcess.class);
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentProcess(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @Override
    public Message<?> doProcess(Message<?> message) {
        LOG.info("Pick PENDING Department started !!!");
        List<Department> list = departmentService.getDepartmentByStatus((String) message.getPayload());
        LOG.info("No. of Departments picked :{}", list.size());
        return MessageBuilder.withPayload(list).copyHeaders(message.getHeaders()).build();
    }
}
