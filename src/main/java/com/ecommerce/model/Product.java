package com.ecommerce.model;

import com.ecommerce.model.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Product entity representing items available for sale
 * Contains product details, pricing, inventory, and relationships
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_slug", columnList = "slug"),
    @Index(name = "idx_product_sku", columnList = "sku"),
    @Index(name = "idx_product_status", columnList = "status"),
    @Index(name = "idx_product_category", columnList = "category_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"category", "images", "reviews", "orderItems", "cartItems", "wishlistItems"})
@ToString(exclude = {"category", "images", "reviews", "orderItems", "cartItems", "wishlistItems"})
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Product name
     */
    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * URL-friendly slug
     */
    @NotBlank(message = "Product slug is required")
    @Size(max = 200, message = "Product slug must not exceed 200 characters")
    @Column(name = "slug", unique = true, nullable = false, length = 200)
    private String slug;

    /**
     * Stock Keeping Unit - unique identifier
     */
    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    @Column(name = "sku", unique = true, nullable = false, length = 50)
    private String sku;

    /**
     * Short product description
     */
    @Size(max = 500, message = "Short description must not exceed 500 characters")
    @Column(name = "short_description", length = 500)
    private String shortDescription;

    /**
     * Detailed product description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Product price
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Discounted price (if on sale)
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be greater than 0")
    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;

    /**
     * Cost price for profit calculation
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    /**
     * Current stock quantity
     */
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    /**
     * Low stock threshold for alerts
     */
    @Min(value = 0, message = "Low stock threshold cannot be negative")
    @Column(name = "low_stock_threshold")
    @Builder.Default
    private Integer lowStockThreshold = 10;

    /**
     * Product weight in kilograms
     */
    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    @Column(name = "weight", precision = 8, scale = 3)
    private BigDecimal weight;

    /**
     * Product dimensions - length in cm
     */
    @DecimalMin(value = "0.0", message = "Length cannot be negative")
    @Column(name = "length", precision = 8, scale = 2)
    private BigDecimal length;

    /**
     * Product dimensions - width in cm
     */
    @DecimalMin(value = "0.0", message = "Width cannot be negative")
    @Column(name = "width", precision = 8, scale = 2)
    private BigDecimal width;

    /**
     * Product dimensions - height in cm
     */
    @DecimalMin(value = "0.0", message = "Height cannot be negative")
    @Column(name = "height", precision = 8, scale = 2)
    private BigDecimal height;

    /**
     * Product status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    /**
     * Whether product is featured
     */
    @Column(name = "featured")
    @Builder.Default
    private Boolean featured = false;

    /**
     * Average rating (0-5)
     */
    @Column(name = "average_rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    /**
     * Number of reviews
     */
    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    /**
     * Number of times product has been viewed
     */
    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    /**
     * Number of times product has been sold
     */
    @Column(name = "sales_count")
    @Builder.Default
    private Long salesCount = 0L;

    /**
     * Product brand
     */
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    @Column(name = "brand", length = 100)
    private String brand;

    /**
     * Manufacturer name
     */
    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    /**
     * Main product image URL
     */
    @Column(name = "main_image_url", length = 500)
    private String mainImageUrl;

    /**
     * Product category
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Product images
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    /**
     * Product reviews
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductReview> reviews = new ArrayList<>();

    /**
     * Order items containing this product
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Cart items containing this product
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * Wishlist items containing this product
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Builder.Default
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    /**
     * Meta title for SEO
     */
    @Column(name = "meta_title", length = 200)
    private String metaTitle;

    /**
     * Meta description for SEO
     */
    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    /**
     * Meta keywords for SEO
     */
    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    /**
     * Get effective price (sale price if available, otherwise regular price)
     * 
     * @return effective price
     */
    public BigDecimal getEffectivePrice() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0 ? salePrice : price;
    }

    /**
     * Check if product is on sale
     * 
     * @return true if product has a sale price
     */
    public boolean isOnSale() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0 && salePrice.compareTo(price) < 0;
    }

    /**
     * Get discount percentage
     * 
     * @return discount percentage
     */
    public BigDecimal getDiscountPercentage() {
        if (!isOnSale()) {
            return BigDecimal.ZERO;
        }
        BigDecimal discount = price.subtract(salePrice);
        return discount.multiply(BigDecimal.valueOf(100)).divide(price, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Check if product is in stock
     * 
     * @return true if stock quantity > 0
     */
    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }

    /**
     * Check if stock is low
     * 
     * @return true if stock is below threshold
     */
    public boolean isLowStock() {
        return stockQuantity != null && lowStockThreshold != null && stockQuantity <= lowStockThreshold;
    }

    /**
     * Add stock quantity
     * 
     * @param quantity quantity to add
     */
    public void addStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot add negative stock");
        }
        this.stockQuantity = (this.stockQuantity == null ? 0 : this.stockQuantity) + quantity;
    }

    /**
     * Remove stock quantity
     * 
     * @param quantity quantity to remove
     * @throws IllegalArgumentException if insufficient stock
     */
    public void removeStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot remove negative stock");
        }
        if (this.stockQuantity == null || this.stockQuantity < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stockQuantity -= quantity;
    }

    /**
     * Increment view count
     */
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0L : this.viewCount) + 1;
    }

    /**
     * Increment sales count
     * 
     * @param quantity quantity sold
     */
    public void incrementSalesCount(int quantity) {
        this.salesCount = (this.salesCount == null ? 0L : this.salesCount) + quantity;
    }

    /**
     * Add a product image
     * 
     * @param image the image to add
     */
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    /**
     * Remove a product image
     * 
     * @param image the image to remove
     */
    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
    }

    /**
     * Add a review
     * 
     * @param review the review to add
     */
    public void addReview(ProductReview review) {
        reviews.add(review);
        review.setProduct(this);
        updateRating();
    }

    /**
     * Update average rating based on reviews
     */
    public void updateRating() {
        if (reviews == null || reviews.isEmpty()) {
            this.averageRating = BigDecimal.ZERO;
            this.reviewCount = 0;
            return;
        }
        
        BigDecimal total = reviews.stream()
            .map(ProductReview::getRating)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.reviewCount = reviews.size();
        this.averageRating = total.divide(BigDecimal.valueOf(reviewCount), 2, BigDecimal.ROUND_HALF_UP);
    }
}
