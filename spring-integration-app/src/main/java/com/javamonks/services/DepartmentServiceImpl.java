package com.javamonks.services;

import com.javamonks.entity.Department;
import com.javamonks.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// Annotation
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Save operation
    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    // Read operation
    @Override
    public Message<List<Department>> fetchDepartmentList() {
        Message<List<Department>> list = MessageBuilder.withPayload((List<Department>)departmentRepository.findAll()).build();
        return  list;
    }

    // Update operation
    @Override
    public Department updateDepartment(Department department, Long departmentId) {
        Department depDB = departmentRepository.findById(departmentId).get();

        if (Objects.nonNull(department.getDepartmentName()) && !"".equalsIgnoreCase(department.getDepartmentName())) {
            depDB.setDepartmentName(department.getDepartmentName());
        }

        if (Objects.nonNull(department.getDepartmentAddress()) && !"".equalsIgnoreCase(department.getDepartmentAddress())) {
            depDB.setDepartmentAddress(department.getDepartmentAddress());
        }

        if (Objects.nonNull(department.getDepartmentCode()) && !"".equalsIgnoreCase(department.getDepartmentCode())) {
            depDB.setDepartmentCode(department.getDepartmentCode());
        }

        if(Objects.nonNull(department.getCode())){
            depDB.setCode(department.getCode());
        }

        if(Objects.nonNull(department.getStatus())){
            depDB.setStatus(department.getStatus());
        }

        depDB.setRetry(department.getRetry());

        return departmentRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteDepartmentById(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public List<Department> getDepartmentByStatus(String status) {
        return departmentRepository.findByStatus(status);
    }
}

