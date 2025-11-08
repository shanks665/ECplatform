package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ShoppingCart entity representing a user's shopping cart
 * Contains cart items and provides cart management functionality
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "shopping_carts", indexes = {
    @Index(name = "idx_cart_user", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "items"})
@ToString(exclude = {"user", "items"})
public class ShoppingCart extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Session ID for guest carts
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;

    /**
     * Last activity timestamp
     */
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    /**
     * Associated user (null for guest carts)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /**
     * Cart items
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    /**
     * Add an item to the cart
     * 
     * @param item the item to add
     */
    public void addItem(CartItem item) {
        // Check if product already exists in cart
        CartItem existingItem = items.stream()
            .filter(ci -> ci.getProduct().getId().equals(item.getProduct().getId()))
            .findFirst()
            .orElse(null);
        
        if (existingItem != null) {
            // Update quantity of existing item
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            existingItem.calculateTotalPrice();
        } else {
            items.add(item);
            item.setCart(this);
        }
        
        updateLastActivity();
    }

    /**
     * Remove an item from the cart
     * 
     * @param item the item to remove
     */
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        updateLastActivity();
    }

    /**
     * Clear all items from the cart
     */
    public void clear() {
        items.clear();
        updateLastActivity();
    }

    /**
     * Get total number of items in cart
     * 
     * @return total item count
     */
    public int getTotalItemCount() {
        return items.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }

    /**
     * Get total number of unique products
     * 
     * @return unique product count
     */
    public int getUniqueItemCount() {
        return items.size();
    }

    /**
     * Calculate cart subtotal
     * 
     * @return subtotal amount
     */
    public BigDecimal getSubtotal() {
        return items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Update last activity timestamp
     */
    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    /**
     * Check if cart is empty
     * 
     * @return true if cart has no items
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    /**
     * Check if cart is expired (inactive for more than specified hours)
     * 
     * @param hours number of hours
     * @return true if cart is expired
     */
    public boolean isExpired(int hours) {
        if (lastActivity == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(lastActivity.plusHours(hours));
    }

    /**
     * Find cart item by product ID
     * 
     * @param productId product ID
     * @return cart item or null
     */
    public CartItem findItemByProductId(Long productId) {
        return items.stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Update item quantity
     * 
     * @param productId product ID
     * @param quantity new quantity
     */
    public void updateItemQuantity(Long productId, int quantity) {
        CartItem item = findItemByProductId(productId);
        if (item != null) {
            if (quantity <= 0) {
                removeItem(item);
            } else {
                item.updateQuantity(quantity);
                updateLastActivity();
            }
        }
    }
}
