package com.javamonks.repo;

import com.javamonks.entity.Department;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Annotation
@Repository

// Interface extending CrudRepository
public interface DepartmentRepository
        extends CrudRepository<Department, Long> {
    List<Department> findByStatus(String status);

    @Query("SELECT p FROM Department p WHERE p.status = :status")
    List<Department> findDepartmentWithStatusLimited(String status, Pageable pageable);
}
