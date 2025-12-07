package com.javamonks.process;

import com.javamonks.entity.Department;
import com.javamonks.integration.flow.EmployeeIntegrationFlows;
import com.javamonks.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DepartmentStatusUpdateProcess implements IntegrationProcess {

    public static final Logger LOG = LoggerFactory.getLogger(DepartmentStatusUpdateProcess.class);

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentStatusUpdateProcess(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Message<?> doProcess(Message<?> message) {
        Department dept = (Department) message.getPayload();
        LOG.info("Department status process start for deptID: {}", dept.getDepartmentId());
        if(dept.getRetry() < 1 ){
            LOG.info("Marking status In Progress");
            dept.setRetry(1);
            dept.setStatus("IN_PROGRESS");
        } else if(dept.getRetry()<=1 && "IN_PROGRESS".equals(dept.getStatus())){
            LOG.info("Marking status Approved!!!");
            dept.setStatus("APPROVED");
        } else{
            dept.setStatus("FAILED");
        }
        LOG.info("Department status process End!!!");
        return MessageBuilder.withPayload(departmentService.updateDepartment(dept, dept.getDepartmentId()))
                .copyHeaders(message.getHeaders())
                .build();
    }
}
