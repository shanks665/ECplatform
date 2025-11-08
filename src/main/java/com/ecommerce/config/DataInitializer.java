package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.model.enums.ProductStatus;
import com.ecommerce.model.enums.UserRole;
import com.ecommerce.model.enums.UserStatus;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data initializer for development and testing
 * Creates sample data on application startup
 * 
 * @author E-Commerce Team
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");

        try {
            // Create admin user
            createAdminUser();
            
            // Create sample categories
            createCategories();
            
            // Create sample products
            createProducts();
            
            logger.info("Sample data initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error initializing sample data", e);
        }
    }

    /**
     * Create admin user
     */
    private void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@ecommerce.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .firstName("System")
                    .lastName("Administrator")
                    .phoneNumber("+1234567890")
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();
            
            userRepository.save(admin);
            logger.info("Admin user created: username=admin, password=Admin@123");
        }

        // Create test customer
        if (!userRepository.existsByUsername("customer")) {
            User customer = User.builder()
                    .username("customer")
                    .email("customer@example.com")
                    .password(passwordEncoder.encode("Customer@123"))
                    .firstName("Test")
                    .lastName("Customer")
                    .phoneNumber("+1234567891")
                    .role(UserRole.CUSTOMER)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();
            
            userRepository.save(customer);
            logger.info("Test customer created: username=customer, password=Customer@123");
        }
    }

    /**
     * Create sample categories
     */
    private void createCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = new ArrayList<>();

            // Electronics
            Category electronics = Category.builder()
                    .name("Electronics")
                    .slug("electronics")
                    .description("Electronic devices and gadgets")
                    .displayOrder(1)
                    .active(true)
                    .build();
            categories.add(electronics);

            // Clothing
            Category clothing = Category.builder()
                    .name("Clothing")
                    .slug("clothing")
                    .description("Fashion and apparel")
                    .displayOrder(2)
                    .active(true)
                    .build();
            categories.add(clothing);

            // Books
            Category books = Category.builder()
                    .name("Books")
                    .slug("books")
                    .description("Books and literature")
                    .displayOrder(3)
                    .active(true)
                    .build();
            categories.add(books);

            // Home & Kitchen
            Category homeKitchen = Category.builder()
                    .name("Home & Kitchen")
                    .slug("home-kitchen")
                    .description("Home and kitchen essentials")
                    .displayOrder(4)
                    .active(true)
                    .build();
            categories.add(homeKitchen);

            // Sports & Outdoors
            Category sports = Category.builder()
                    .name("Sports & Outdoors")
                    .slug("sports-outdoors")
                    .description("Sports equipment and outdoor gear")
                    .displayOrder(5)
                    .active(true)
                    .build();
            categories.add(sports);

            categoryRepository.saveAll(categories);
            logger.info("Created {} categories", categories.size());
        }
    }

    /**
     * Create sample products
     */
    private void createProducts() {
        if (productRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                logger.warn("No categories found, skipping product creation");
                return;
            }

            List<Product> products = new ArrayList<>();

            // Electronics products
            Category electronics = categoryRepository.findBySlug("electronics").orElse(null);
            if (electronics != null) {
                products.add(createProduct("Laptop Pro 15", "laptop-pro-15", "LPT-001", 
                    "High-performance laptop for professionals", electronics, 
                    new BigDecimal("1299.99"), new BigDecimal("1199.99"), 50));

                products.add(createProduct("Wireless Mouse", "wireless-mouse", "WMS-001",
                    "Ergonomic wireless mouse with precision tracking", electronics,
                    new BigDecimal("29.99"), null, 200));

                products.add(createProduct("Bluetooth Headphones", "bluetooth-headphones", "BTH-001",
                    "Premium noise-cancelling headphones", electronics,
                    new BigDecimal("199.99"), new BigDecimal("179.99"), 100));
            }

            // Clothing products
            Category clothing = categoryRepository.findBySlug("clothing").orElse(null);
            if (clothing != null) {
                products.add(createProduct("Classic T-Shirt", "classic-t-shirt", "TSH-001",
                    "Comfortable cotton t-shirt", clothing,
                    new BigDecimal("24.99"), null, 500));

                products.add(createProduct("Denim Jeans", "denim-jeans", "JNS-001",
                    "Premium quality denim jeans", clothing,
                    new BigDecimal("59.99"), new BigDecimal("49.99"), 300));
            }

            // Books
            Category books = categoryRepository.findBySlug("books").orElse(null);
            if (books != null) {
                products.add(createProduct("Programming in Java", "programming-in-java", "BK-001",
                    "Comprehensive guide to Java programming", books,
                    new BigDecimal("49.99"), null, 150));

                products.add(createProduct("Web Development Essentials", "web-development-essentials", "BK-002",
                    "Learn modern web development", books,
                    new BigDecimal("39.99"), new BigDecimal("34.99"), 200));
            }

            // Home & Kitchen
            Category homeKitchen = categoryRepository.findBySlug("home-kitchen").orElse(null);
            if (homeKitchen != null) {
                products.add(createProduct("Coffee Maker", "coffee-maker", "CFM-001",
                    "Programmable coffee maker with timer", homeKitchen,
                    new BigDecimal("79.99"), null, 75));

                products.add(createProduct("Blender Pro", "blender-pro", "BLD-001",
                    "High-speed blender for smoothies and more", homeKitchen,
                    new BigDecimal("99.99"), new BigDecimal("89.99"), 100));
            }

            // Sports & Outdoors
            Category sports = categoryRepository.findBySlug("sports-outdoors").orElse(null);
            if (sports != null) {
                products.add(createProduct("Yoga Mat", "yoga-mat", "YGM-001",
                    "Non-slip yoga mat with carrying strap", sports,
                    new BigDecimal("34.99"), null, 250));

                products.add(createProduct("Running Shoes", "running-shoes", "RNS-001",
                    "Lightweight running shoes with cushioned sole", sports,
                    new BigDecimal("89.99"), new BigDecimal("79.99"), 180));
            }

            productRepository.saveAll(products);
            logger.info("Created {} products", products.size());
        }
    }

    /**
     * Helper method to create a product
     */
    private Product createProduct(String name, String slug, String sku, String description,
                                 Category category, BigDecimal price, BigDecimal salePrice, int stock) {
        return Product.builder()
                .name(name)
                .slug(slug)
                .sku(sku)
                .shortDescription(description)
                .description("Detailed description for " + name + ". " + description)
                .price(price)
                .salePrice(salePrice)
                .cost(price.multiply(BigDecimal.valueOf(0.6)))
                .stockQuantity(stock)
                .lowStockThreshold(10)
                .category(category)
                .status(ProductStatus.ACTIVE)
                .featured(Math.random() > 0.7)
                .averageRating(BigDecimal.valueOf(4.0 + Math.random()))
                .reviewCount((int) (Math.random() * 50))
                .viewCount((long) (Math.random() * 1000))
                .salesCount((long) (Math.random() * 500))
                .brand("BrandName")
                .manufacturer("ManufacturerName")
                .weight(BigDecimal.valueOf(Math.random() * 5))
                .build();
    }
}
