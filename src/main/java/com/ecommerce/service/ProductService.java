package com.ecommerce.service;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.request.CreateProductRequest;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.enums.ProductStatus;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.util.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service for product management operations
 * 
 * @author E-Commerce Team
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SlugGenerator slugGenerator;

    /**
     * Create a new product
     * 
     * @param request product creation request
     * @return created product
     */
    public Product createProduct(CreateProductRequest request) {
        // Check if SKU already exists
        if (productRepository.existsBySku(request.getSku())) {
            throw new BadRequestException("Product with SKU " + request.getSku() + " already exists");
        }

        // Get category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        // Generate slug
        String baseSlug = slugGenerator.generateSlug(request.getName());
        String slug = baseSlug;
        int suffix = 1;
        while (productRepository.existsBySlug(slug)) {
            slug = slugGenerator.generateUniqueSlug(baseSlug, suffix++);
        }

        // Create product
        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .sku(request.getSku())
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .price(request.getPrice())
                .salePrice(request.getSalePrice())
                .cost(request.getCost())
                .stockQuantity(request.getStockQuantity())
                .lowStockThreshold(request.getLowStockThreshold() != null ? request.getLowStockThreshold() : 10)
                .weight(request.getWeight())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .brand(request.getBrand())
                .manufacturer(request.getManufacturer())
                .category(category)
                .status(ProductStatus.DRAFT)
                .featured(false)
                .averageRating(BigDecimal.ZERO)
                .reviewCount(0)
                .viewCount(0L)
                .salesCount(0L)
                .build();

        return productRepository.save(product);
    }

    /**
     * Get product by ID
     * 
     * @param productId product ID
     * @return product
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }

    /**
     * Get product by slug
     * 
     * @param slug product slug
     * @return product
     */
    @Transactional(readOnly = true)
    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
    }

    /**
     * Get all products
     * 
     * @param pageable pagination information
     * @return page of products
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Get active products
     * 
     * @param pageable pagination information
     * @return page of active products
     */
    @Transactional(readOnly = true)
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productRepository.findByStatus(ProductStatus.ACTIVE, pageable);
    }

    /**
     * Get products by category
     * 
     * @param categoryId category ID
     * @param pageable pagination information
     * @return page of products
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndStatus(categoryId, ProductStatus.ACTIVE, pageable);
    }

    /**
     * Search products
     * 
     * @param keyword search keyword
     * @param pageable pagination information
     * @return page of products
     */
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable);
    }

    /**
     * Get featured products
     * 
     * @param pageable pagination information
     * @return page of featured products
     */
    @Transactional(readOnly = true)
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.findFeaturedProducts(pageable);
    }

    /**
     * Get products on sale
     * 
     * @param pageable pagination information
     * @return page of products on sale
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsOnSale(Pageable pageable) {
        return productRepository.findProductsOnSale(pageable);
    }

    /**
     * Get top selling products
     * 
     * @param pageable pagination information
     * @return page of top selling products
     */
    @Transactional(readOnly = true)
    public Page<Product> getTopSellingProducts(Pageable pageable) {
        return productRepository.findTopSellingProducts(pageable);
    }

    /**
     * Update product
     * 
     * @param productId product ID
     * @param request update request
     * @return updated product
     */
    public Product updateProduct(Long productId, CreateProductRequest request) {
        Product product = getProductById(productId);

        if (request.getName() != null && !request.getName().equals(product.getName())) {
            product.setName(request.getName());
            // Regenerate slug if name changed
            String baseSlug = slugGenerator.generateSlug(request.getName());
            String slug = baseSlug;
            int suffix = 1;
            while (productRepository.existsBySlug(slug) && !slug.equals(product.getSlug())) {
                slug = slugGenerator.generateUniqueSlug(baseSlug, suffix++);
            }
            product.setSlug(slug);
        }

        if (request.getShortDescription() != null) {
            product.setShortDescription(request.getShortDescription());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getSalePrice() != null) {
            product.setSalePrice(request.getSalePrice());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getManufacturer() != null) {
            product.setManufacturer(request.getManufacturer());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    /**
     * Update product status
     * 
     * @param productId product ID
     * @param status new status
     * @return updated product
     */
    public Product updateProductStatus(Long productId, ProductStatus status) {
        Product product = getProductById(productId);
        product.setStatus(status);
        return productRepository.save(product);
    }

    /**
     * Add stock to product
     * 
     * @param productId product ID
     * @param quantity quantity to add
     */
    public void addStock(Long productId, int quantity) {
        Product product = getProductById(productId);
        product.addStock(quantity);
        productRepository.save(product);
    }

    /**
     * Remove stock from product
     * 
     * @param productId product ID
     * @param quantity quantity to remove
     */
    public void removeStock(Long productId, int quantity) {
        Product product = getProductById(productId);
        product.removeStock(quantity);
        productRepository.save(product);
    }

    /**
     * Increment product view count
     * 
     * @param productId product ID
     */
    public void incrementViewCount(Long productId) {
        productRepository.incrementViewCount(productId);
    }

    /**
     * Delete product (soft delete)
     * 
     * @param productId product ID
     */
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        product.softDelete();
        productRepository.save(product);
    }

    /**
     * Convert Product entity to ProductDTO
     * 
     * @param product product entity
     * @return product DTO
     */
    public ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .sku(product.getSku())
                .shortDescription(product.getShortDescription())
                .description(product.getDescription())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .stockQuantity(product.getStockQuantity())
                .status(product.getStatus())
                .featured(product.getFeatured())
                .averageRating(product.getAverageRating())
                .reviewCount(product.getReviewCount())
                .viewCount(product.getViewCount())
                .salesCount(product.getSalesCount())
                .brand(product.getBrand())
                .manufacturer(product.getManufacturer())
                .mainImageUrl(product.getMainImageUrl())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
