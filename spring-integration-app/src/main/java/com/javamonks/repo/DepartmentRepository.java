package com.javamonks.repo;

import com.javamonks.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository

// Interface extending CrudRepository
public interface DepartmentRepository
        extends CrudRepository<Department, Long> {
}
