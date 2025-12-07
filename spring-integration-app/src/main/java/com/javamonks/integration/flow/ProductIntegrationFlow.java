package com.javamonks.integration.flow;

import com.javamonks.dto.ProductDto;
import com.javamonks.entity.Product;
import com.javamonks.process.PickNewProductProcess;
import com.javamonks.process.UpdateProductStatusProcess;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class ProductIntegrationFlow {

    public static final Logger LOG = LoggerFactory.getLogger(PickNewProductProcess.class);

    private final PickNewProductProcess pickNewProductProcess;

    private final UpdateProductStatusProcess updateProductStatusProcess;

    private final ThreadPoolTaskExecutor taskExecutor;

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public ProductIntegrationFlow(PickNewProductProcess pickNewProductProcess, UpdateProductStatusProcess updateProductStatusProcess, ThreadPoolTaskExecutor taskExecutor, EntityManagerFactory entityManagerFactory) {
        this.pickNewProductProcess = pickNewProductProcess;
        this.updateProductStatusProcess = updateProductStatusProcess;
        this.taskExecutor = taskExecutor;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }


    /**
     * 1. Define the inbound JPA polling adapter (data source).
     */
    @Bean
    public IntegrationFlow fetchProductsFlow() {
        return IntegrationFlow
                .fromSupplier(() -> pickNewProductProcess.doProcess(MessageBuilder.withPayload("NEW").build()),
                        c -> c.poller(Pollers.fixedRate(30000).transactional(transactionManager()))
                ).enrichHeaders(h -> {
                    h.header(MessageHeaders.ERROR_CHANNEL, "errorHandlerFlow");
                })
                .split()
                // Channel: 'fetchedProductsChannel' (implicit)

                // Transformer: The payload is currently a List<Product>, transform to a single Product
                .<Product, ProductDto>transform(data -> {
                    ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(data, dto);
                    return dto;
                })
                .channel(c -> c.executor(taskExecutor))

                // Router: Decide the next channel based on payload content
                .route(ProductDto.class, ProductDto -> {
                    if (ProductDto.getPrice() > 100.00) {
                        return "highValueChannel";
                    } else {
                        return "lowValueChannel";
                    }
                })
                .get();
    }

    /**
     * 2A. High Value Flow (Handle, Transform, Log)
     */
    @Bean
    public IntegrationFlow highValueProductFlow() {
        return IntegrationFlow.from("highValueChannel")
                // Handler (Service Activator): Business Logic
                .handle(ProductDto.class, (product, headers) -> {
                    LOG.info("Processing HIGH Value Product: {}" , product.getName());
                    product.setStatus("PROCESSED_HIGH_VALUE");
                    return product; // Return the modified entity
                })
                // Log component
                .log()
                // Send to the final update channel
                .channel("jpaUpdateChannel")
                .get();
    }

    /**
     * 2B. Low Value Flow (Handle, Transform, Log)
     */
    @Bean
    public IntegrationFlow lowValueProductFlow() {
        return IntegrationFlow.from("lowValueChannel")
                // Handler (Service Activator): Business Logic
                .handle(ProductDto.class, (product, headers) -> {
                    LOG.info("Processing LOW Value Product: {}" , product.getName());
                    product.setStatus("PROCESSED_LOW_VALUE");
                    return product; // Return the modified entity
                })
                // Log component
                .log()
                // Send to the final update channel
                .channel("jpaUpdateChannel")
                .get();
    }

    /**
     * 3. OUTBOUND JPA GATEWAY: Persists changes to the database.
     */
    @Bean
    public IntegrationFlow updateDbFlow() {
        return IntegrationFlow.from("jpaUpdateChannel")
                // Use a JpaOutboundGateway to merge the updated entity state
                .handle(updateProductStatusProcess,"doProcess") // MERGE handles updates to existing entities
                .handle(message -> {
                    LOG.info("Completed successfully!!");
                })
                .get();
    }
}
