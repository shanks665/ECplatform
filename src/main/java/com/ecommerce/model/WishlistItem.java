package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * WishlistItem entity representing items in a user's wishlist
 * Allows users to save products for later purchase
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "wishlist_items", indexes = {
    @Index(name = "idx_wishlist_user", columnList = "user_id"),
    @Index(name = "idx_wishlist_product", columnList = "product_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_product", columnNames = {"user_id", "product_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "product"})
@ToString(exclude = {"user", "product"})
public class WishlistItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Priority or notes for the item
     */
    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Whether to notify user when product is on sale
     */
    @Column(name = "notify_on_sale")
    @Builder.Default
    private Boolean notifyOnSale = false;

    /**
     * Whether to notify user when product is back in stock
     */
    @Column(name = "notify_on_restock")
    @Builder.Default
    private Boolean notifyOnRestock = false;

    /**
     * Associated user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Associated product
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Check if product is currently available
     * 
     * @return true if product is in stock
     */
    public boolean isProductAvailable() {
        return product != null && product.isInStock();
    }

    /**
     * Check if product is on sale
     * 
     * @return true if product has a sale price
     */
    public boolean isProductOnSale() {
        return product != null && product.isOnSale();
    }
}
