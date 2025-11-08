package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for E-Commerce Platform
 * Comprehensive e-commerce system with user management, product catalog,
 * shopping cart, order processing, and admin features
 * 
 * @author E-Commerce Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class EcommercePlatformApplication {

    /**
     * Main entry point for the application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EcommercePlatformApplication.class, args);
        System.out.println("==============================================");
        System.out.println("E-Commerce Platform Started Successfully!");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("==============================================");
    }
}
