package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * ProductReview entity representing customer reviews for products
 * Includes rating, title, comment, and verification status
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "product_reviews", indexes = {
    @Index(name = "idx_review_product", columnList = "product_id"),
    @Index(name = "idx_review_user", columnList = "user_id"),
    @Index(name = "idx_review_rating", columnList = "rating"),
    @Index(name = "idx_review_verified", columnList = "verified_purchase")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"product", "user"})
@ToString(exclude = {"product", "user"})
public class ProductReview extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Review rating (1-5)
     */
    @NotNull(message = "Rating is required")
    @DecimalMin(value = "1.0", message = "Rating must be at least 1")
    @DecimalMax(value = "5.0", message = "Rating must not exceed 5")
    @Column(name = "rating", nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    /**
     * Review title
     */
    @NotBlank(message = "Review title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * Review comment
     */
    @NotBlank(message = "Review comment is required")
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    /**
     * Whether this is a verified purchase
     */
    @Column(name = "verified_purchase")
    @Builder.Default
    private Boolean verifiedPurchase = false;

    /**
     * Number of helpful votes
     */
    @Column(name = "helpful_count")
    @Builder.Default
    private Integer helpfulCount = 0;

    /**
     * Number of not helpful votes
     */
    @Column(name = "not_helpful_count")
    @Builder.Default
    private Integer notHelpfulCount = 0;

    /**
     * Whether review is approved
     */
    @Column(name = "approved")
    @Builder.Default
    private Boolean approved = false;

    /**
     * Associated product
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * User who wrote the review
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Increment helpful count
     */
    public void incrementHelpfulCount() {
        this.helpfulCount = (this.helpfulCount == null ? 0 : this.helpfulCount) + 1;
    }

    /**
     * Increment not helpful count
     */
    public void incrementNotHelpfulCount() {
        this.notHelpfulCount = (this.notHelpfulCount == null ? 0 : this.notHelpfulCount) + 1;
    }

    /**
     * Get helpfulness score
     * 
     * @return helpfulness score (helpful - not helpful)
     */
    public int getHelpfulnessScore() {
        int helpful = this.helpfulCount == null ? 0 : this.helpfulCount;
        int notHelpful = this.notHelpfulCount == null ? 0 : this.notHelpfulCount;
        return helpful - notHelpful;
    }
}
