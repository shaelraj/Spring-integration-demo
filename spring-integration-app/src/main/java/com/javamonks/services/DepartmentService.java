package com.javamonks.services;
// Importing required classes
import com.javamonks.entity.Department;
import org.springframework.messaging.Message;

import java.util.List;

// Interface
public interface DepartmentService {

    // Save operation
    Department saveDepartment(Department department);

    // Read operation
    Message<List<Department>> fetchDepartmentList();

    // Update operation
    Department updateDepartment(Department department,
                                Long departmentId);

    // Delete operation
    void deleteDepartmentById(Long departmentId);
}

