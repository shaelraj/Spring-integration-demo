package com.javamonks.repo;

import com.javamonks.entity.Department;
import com.javamonks.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(String status);

    @Query("SELECT p FROM Product p WHERE p.status = :status")
    List<Product> findProductsWithStatusLimited(String status, Pageable pageable);
}

