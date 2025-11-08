package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * CartItem entity representing individual items in a shopping cart
 * Links products to shopping carts with quantity and price information
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
    @Index(name = "idx_cart_item_product", columnList = "product_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_cart_product", columnNames = {"cart_id", "product_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"cart", "product"})
@ToString(exclude = {"cart", "product"})
public class CartItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Quantity in cart
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Unit price at the time item was added
     */
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Total price for this cart item
     */
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Associated shopping cart
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart cart;

    /**
     * Associated product
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Calculate total price based on quantity and unit price
     */
    public void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Update quantity and recalculate total
     * 
     * @param newQuantity new quantity
     */
    public void updateQuantity(int newQuantity) {
        if (newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = newQuantity;
        calculateTotalPrice();
    }

    /**
     * Increment quantity by 1
     */
    public void incrementQuantity() {
        this.quantity++;
        calculateTotalPrice();
    }

    /**
     * Decrement quantity by 1
     */
    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
            calculateTotalPrice();
        }
    }

    /**
     * Update unit price to current product price
     */
    public void updatePrice() {
        if (product != null) {
            this.unitPrice = product.getEffectivePrice();
            calculateTotalPrice();
        }
    }

    /**
     * Check if item is in stock
     * 
     * @return true if sufficient stock available
     */
    public boolean isInStock() {
        return product != null && product.getStockQuantity() >= quantity;
    }
}
