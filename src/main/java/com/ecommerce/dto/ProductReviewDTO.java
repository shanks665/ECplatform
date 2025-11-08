package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for ProductReview entity
 * 
 * @author E-Commerce Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewDTO {
    private Long id;
    private BigDecimal rating;
    private String title;
    private String comment;
    private Boolean verifiedPurchase;
    private Integer helpfulCount;
    private Integer notHelpfulCount;
    private Boolean approved;
    private Long productId;
    private String productName;
    private Long userId;
    private String userFullName;
    private LocalDateTime createdAt;
}
