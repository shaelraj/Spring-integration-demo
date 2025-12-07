package com.javamonks.process;

import com.javamonks.entity.Product;
import com.javamonks.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PickNewProductProcess implements IntegrationProcess {

    public static final Logger LOG = LoggerFactory.getLogger(PickNewProductProcess.class);

    private final ProductService productService;

    public PickNewProductProcess(ProductService productService) {
        this.productService = productService;
    }


    @Override
    public Message<?> doProcess(Message<?> message) {
        LOG.info("Pick New product started !!!");
        List<Product> list =productService.getProductByStatus((String) message.getPayload());
        LOG.info("No. of Product picked :{}", list.size());
        return MessageBuilder.withPayload(list).copyHeaders(message.getHeaders()).build();
    }
}
