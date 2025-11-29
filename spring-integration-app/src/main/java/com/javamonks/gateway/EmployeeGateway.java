package com.javamonks.gateway;

import com.javamonks.model.Employee;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface EmployeeGateway {

    @Gateway(requestChannel = "req-emp-name-channel")
    public String getEmployeeName(String name);

    @Gateway(requestChannel = "req-hire-emp-channel")
    public Message<Employee> hireEmployee(Employee employee);

    //#########################  TRANSFORMER  ########################//

    @Gateway(requestChannel = "emp-status-channel")
    public String processEmployeeStatus(String status);

    //#########################  SPLITTER  ########################//

    @Gateway(requestChannel = "emp-managers-channel")
    public String getManagerList(String managers);

    //#########################  FILTER  ########################//

    @Gateway(requestChannel = "dev-emp-channel")
    public String getEmployeeIfADeveloper(String empDesignation);


    //#########################  ROUTER  ########################//

    @Gateway(requestChannel = "emp-dep-channel")
    public String getEmployeeDepartment(Employee emmployee);


}
