package com.javamonks.repo;

import com.javamonks.entity.Department;
import com.javamonks.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(String status);
}

