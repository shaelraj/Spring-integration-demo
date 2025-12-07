package com.javamonks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@SpringBootApplication
@EnableIntegrationGraphController(allowedOrigins = "*") // Allows access from any viewer URL
public class SpringIntegrationAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationAppApplication.class, args);
	}

}
