package com.ecommerce.repository;

import com.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProductImage entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * Find images by product ID
     * 
     * @param productId product ID
     * @return list of images ordered by display order
     */
    @Query("SELECT i FROM ProductImage i WHERE i.product.id = :productId ORDER BY i.displayOrder, i.createdAt")
    List<ProductImage> findByProductId(@Param("productId") Long productId);

    /**
     * Find primary image for product
     * 
     * @param productId product ID
     * @return Optional containing primary image if found
     */
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);

    /**
     * Unset primary image for product
     * 
     * @param productId product ID
     */
    @Modifying
    @Query("UPDATE ProductImage i SET i.isPrimary = false WHERE i.product.id = :productId")
    void unsetPrimaryForProduct(@Param("productId") Long productId);

    /**
     * Count images by product
     * 
     * @param productId product ID
     * @return count of images
     */
    long countByProductId(Long productId);

    /**
     * Delete images by product ID
     * 
     * @param productId product ID
     */
    void deleteByProductId(Long productId);
}
