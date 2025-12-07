package com.javamonks.services;

import com.javamonks.entity.Product;
import org.springframework.messaging.Message;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    // Read operation
    Message<List<Product>> fetchProductList();

    // Update operation
    Product updateProduct(Product product,
                             Long productId);

    // Delete operation
    void deleteProductById(Long productId);

    List<Product> getProductByStatus(String status);
}
