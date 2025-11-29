package com.javamonks.controller;

import com.javamonks.gateway.EmployeeGateway;
import com.javamonks.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrate")
public class EmployeeController {

    @Autowired
    public EmployeeGateway employeeGateway;

    @GetMapping(value ="{name}")
    public String getEmployeeNameFromService(@PathVariable("name") String name){
        return employeeGateway.getEmployeeName(name);
    }

    @PostMapping(value="hireEmp")
    public Employee hireEmployee(@RequestBody Employee employee){
        Message<Employee> message = employeeGateway.hireEmployee(employee);
        return message.getPayload();
    }


    //#########################  TRANSFORMER  ########################//

    @GetMapping(value = "/processEmployeeStatus/{status}")
    public String processEmployeeStatus(@PathVariable("status") String status) {
        return employeeGateway.processEmployeeStatus(status);
    }


    //#########################  SPLITTER  ########################//

    @GetMapping(value = "/getManagerList/{managers}")
    public String getManagerList(@PathVariable("managers") String managers) {
        return employeeGateway.getManagerList(managers);
    }

    //#########################  FILTER  ########################//

    @GetMapping(value = "/getEmployeeIfADeveloper/{empDesignation}")
    public String getEmployeeIfADeveloper(@PathVariable("empDesignation") String empDesignation) {
        return employeeGateway.getEmployeeIfADeveloper(empDesignation);
    }
}
