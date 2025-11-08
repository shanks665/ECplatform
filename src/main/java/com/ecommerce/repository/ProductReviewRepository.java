package com.ecommerce.repository;

import com.ecommerce.model.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProductReview entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    /**
     * Find reviews by product ID
     * 
     * @param productId product ID
     * @param pageable pagination information
     * @return page of reviews
     */
    Page<ProductReview> findByProductId(Long productId, Pageable pageable);

    /**
     * Find approved reviews by product ID
     * 
     * @param productId product ID
     * @param pageable pagination information
     * @return page of approved reviews
     */
    Page<ProductReview> findByProductIdAndApprovedTrue(Long productId, Pageable pageable);

    /**
     * Find reviews by user ID
     * 
     * @param userId user ID
     * @param pageable pagination information
     * @return page of reviews
     */
    Page<ProductReview> findByUserId(Long userId, Pageable pageable);

    /**
     * Find review by user and product
     * 
     * @param userId user ID
     * @param productId product ID
     * @return Optional containing review if found
     */
    Optional<ProductReview> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * Check if user has reviewed product
     * 
     * @param userId user ID
     * @param productId product ID
     * @return true if user has reviewed the product
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    /**
     * Find pending reviews
     * 
     * @param pageable pagination information
     * @return page of pending reviews
     */
    Page<ProductReview> findByApprovedFalse(Pageable pageable);

    /**
     * Find verified purchase reviews
     * 
     * @param productId product ID
     * @param pageable pagination information
     * @return page of verified reviews
     */
    Page<ProductReview> findByProductIdAndVerifiedPurchaseTrue(Long productId, Pageable pageable);

    /**
     * Get average rating for product
     * 
     * @param productId product ID
     * @return average rating
     */
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId AND r.approved = true")
    BigDecimal getAverageRating(@Param("productId") Long productId);

    /**
     * Count reviews by product
     * 
     * @param productId product ID
     * @return count of reviews
     */
    long countByProductId(Long productId);

    /**
     * Count approved reviews by product
     * 
     * @param productId product ID
     * @return count of approved reviews
     */
    long countByProductIdAndApprovedTrue(Long productId);

    /**
     * Find most helpful reviews
     * 
     * @param productId product ID
     * @param pageable pagination information
     * @return page of helpful reviews
     */
    @Query("SELECT r FROM ProductReview r WHERE r.product.id = :productId AND r.approved = true ORDER BY (r.helpfulCount - r.notHelpfulCount) DESC")
    Page<ProductReview> findMostHelpfulReviews(@Param("productId") Long productId, Pageable pageable);

    /**
     * Find reviews by rating
     * 
     * @param productId product ID
     * @param rating rating value
     * @param pageable pagination information
     * @return page of reviews
     */
    Page<ProductReview> findByProductIdAndRatingAndApprovedTrue(Long productId, BigDecimal rating, Pageable pageable);
}
