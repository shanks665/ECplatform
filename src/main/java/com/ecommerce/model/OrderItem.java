package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * OrderItem entity representing individual items in an order
 * Links products to orders with quantity and price information
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_item_order", columnList = "order_id"),
    @Index(name = "idx_order_item_product", columnList = "product_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"order", "product"})
@ToString(exclude = {"order", "product"})
public class OrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Quantity ordered
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Unit price at the time of order
     */
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Total price for this item (quantity * unit price)
     */
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Discount applied to this item
     */
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @Column(name = "discount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * Tax amount for this item
     */
    @DecimalMin(value = "0.0", message = "Tax cannot be negative")
    @Column(name = "tax", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;

    /**
     * Product name snapshot at time of order
     */
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    /**
     * Product SKU snapshot at time of order
     */
    @Column(name = "product_sku", length = 50)
    private String productSku;

    /**
     * Associated order
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

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
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity))
            .subtract(discount != null ? discount : BigDecimal.ZERO)
            .add(tax != null ? tax : BigDecimal.ZERO);
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
     * Apply discount to this item
     * 
     * @param discountAmount discount amount to apply
     */
    public void applyDiscount(BigDecimal discountAmount) {
        if (discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        this.discount = discountAmount;
        calculateTotalPrice();
    }

    /**
     * Get subtotal before tax and discount
     * 
     * @return subtotal amount
     */
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
