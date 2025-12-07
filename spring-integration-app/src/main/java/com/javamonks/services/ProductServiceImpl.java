package com.javamonks.services;

import com.javamonks.entity.Product;
import com.javamonks.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Message<List<Product>> fetchProductList() {
        Message<List<Product>> list = MessageBuilder.withPayload((List<Product>) productRepository.findAll()).build();
        return list;
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        Optional<Product> optional = productRepository.findById(productId);
        if (optional.isEmpty()) {
            throw new RuntimeException("Product not found for productID: " + productId);
        }
        Product updatedProduct = optional.get();
        if (Objects.nonNull(product.getName())) {
            updatedProduct.setName(product.getName());
        }
        if (Objects.nonNull(product.getStatus())) {
            updatedProduct.setStatus(product.getStatus());
        }
        if (Objects.nonNull(product.getPrice())) {
            updatedProduct.setPrice(product.getPrice());
        }

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getProductByStatus(String status) {
        Pageable limit = PageRequest.of(0, 15, Sort.by("id").ascending());
        return productRepository.findProductsWithStatusLimited(status,limit);
    }
}
