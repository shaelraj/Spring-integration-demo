package com.javamonks.process;

import com.javamonks.dto.ProductDto;
import com.javamonks.entity.Product;
import com.javamonks.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductStatusProcess implements IntegrationProcess {

    public static final Logger LOG = LoggerFactory.getLogger(UpdateProductStatusProcess.class);

    private final ProductService productService;

    public UpdateProductStatusProcess(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Message<?> doProcess(Message<?> message) {
        ProductDto productDto = (ProductDto) message.getPayload();
        LOG.info("update Product process started for :{}", productDto.getId());
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return MessageBuilder.withPayload(productService.updateProduct(product, product.getId()))
                .copyHeaders(message.getHeaders())
                .build();
    }
}
