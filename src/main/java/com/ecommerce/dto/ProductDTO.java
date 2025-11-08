package com.ecommerce.dto;

import com.ecommerce.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Product entity
 * 
 * @author E-Commerce Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String slug;
    private String sku;
    private String shortDescription;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer stockQuantity;
    private ProductStatus status;
    private Boolean featured;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private Long viewCount;
    private Long salesCount;
    private String brand;
    private String manufacturer;
    private String mainImageUrl;
    private Long categoryId;
    private String categoryName;
    @Builder.Default
    private List<ProductImageDTO> images = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public BigDecimal getEffectivePrice() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0 ? salePrice : price;
    }
    
    public boolean isOnSale() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0 && salePrice.compareTo(price) < 0;
    }
    
    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }
}
