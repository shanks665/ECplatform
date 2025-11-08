package com.ecommerce.repository;

import com.ecommerce.model.Product;
import com.ecommerce.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity
 * Provides database operations for product management
 * 
 * @author E-Commerce Team
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Find product by slug
     * 
     * @param slug product slug
     * @return Optional containing product if found
     */
    Optional<Product> findBySlug(String slug);

    /**
     * Find product by SKU
     * 
     * @param sku product SKU
     * @return Optional containing product if found
     */
    Optional<Product> findBySku(String sku);

    /**
     * Check if SKU exists
     * 
     * @param sku SKU to check
     * @return true if SKU exists
     */
    boolean existsBySku(String sku);

    /**
     * Check if slug exists
     * 
     * @param slug slug to check
     * @return true if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find products by status
     * 
     * @param status product status
     * @param pageable pagination information
     * @return page of products
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    /**
     * Find products by category ID
     * 
     * @param categoryId category ID
     * @param pageable pagination information
     * @return page of products
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Find products by category ID and status
     * 
     * @param categoryId category ID
     * @param status product status
     * @param pageable pagination information
     * @return page of products
     */
    Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

    /**
     * Find featured products
     * 
     * @param pageable pagination information
     * @return page of featured products
     */
    @Query("SELECT p FROM Product p WHERE p.featured = true AND p.status = 'ACTIVE' AND p.deleted = false")
    Page<Product> findFeaturedProducts(Pageable pageable);

    /**
     * Find products on sale
     * 
     * @param pageable pagination information
     * @return page of products on sale
     */
    @Query("SELECT p FROM Product p WHERE p.salePrice IS NOT NULL AND p.salePrice > 0 AND p.salePrice < p.price AND p.status = 'ACTIVE' AND p.deleted = false")
    Page<Product> findProductsOnSale(Pageable pageable);

    /**
     * Find products in price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @param pageable pagination information
     * @return page of products
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.status = 'ACTIVE' AND p.deleted = false")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                    @Param("maxPrice") BigDecimal maxPrice, 
                                    Pageable pageable);

    /**
     * Search products by keyword
     * 
     * @param keyword search keyword
     * @param pageable pagination information
     * @return page of products
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "p.status = 'ACTIVE' AND p.deleted = false")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Find products with low stock
     * 
     * @return list of products with low stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.lowStockThreshold AND p.status = 'ACTIVE' AND p.deleted = false")
    List<Product> findLowStockProducts();

    /**
     * Find out of stock products
     * 
     * @return list of out of stock products
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 AND p.deleted = false")
    List<Product> findOutOfStockProducts();

    /**
     * Find top selling products
     * 
     * @param pageable pagination information
     * @return page of top selling products
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.deleted = false ORDER BY p.salesCount DESC")
    Page<Product> findTopSellingProducts(Pageable pageable);

    /**
     * Find most viewed products
     * 
     * @param pageable pagination information
     * @return page of most viewed products
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.deleted = false ORDER BY p.viewCount DESC")
    Page<Product> findMostViewedProducts(Pageable pageable);

    /**
     * Find top rated products
     * 
     * @param pageable pagination information
     * @return page of top rated products
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.deleted = false AND p.reviewCount > 0 ORDER BY p.averageRating DESC, p.reviewCount DESC")
    Page<Product> findTopRatedProducts(Pageable pageable);

    /**
     * Find new arrivals
     * 
     * @param pageable pagination information
     * @return page of new products
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.deleted = false ORDER BY p.createdAt DESC")
    Page<Product> findNewArrivals(Pageable pageable);

    /**
     * Find products by brand
     * 
     * @param brand brand name
     * @param pageable pagination information
     * @return page of products
     */
    Page<Product> findByBrandAndStatus(String brand, ProductStatus status, Pageable pageable);

    /**
     * Find all brands
     * 
     * @return list of brand names
     */
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL AND p.deleted = false ORDER BY p.brand")
    List<String> findAllBrands();

    /**
     * Increment view count
     * 
     * @param productId product ID
     */
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :productId")
    void incrementViewCount(@Param("productId") Long productId);

    /**
     * Update stock quantity
     * 
     * @param productId product ID
     * @param quantity quantity to add (negative to subtract)
     */
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :productId")
    void updateStockQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);

    /**
     * Count products by status
     * 
     * @param status product status
     * @return count of products
     */
    long countByStatus(ProductStatus status);

    /**
     * Count products by category
     * 
     * @param categoryId category ID
     * @return count of products
     */
    long countByCategoryId(Long categoryId);
}
