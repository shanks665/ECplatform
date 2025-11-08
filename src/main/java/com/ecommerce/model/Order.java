package com.ecommerce.model;

import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.model.enums.PaymentMethod;
import com.ecommerce.model.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing customer orders
 * Contains order details, items, pricing, and status tracking
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_number", columnList = "order_number"),
    @Index(name = "idx_order_user", columnList = "user_id"),
    @Index(name = "idx_order_status", columnList = "status"),
    @Index(name = "idx_order_date", columnList = "order_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "items", "shippingAddress", "billingAddress"})
@ToString(exclude = {"user", "items", "shippingAddress", "billingAddress"})
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Unique order number
     */
    @NotBlank(message = "Order number is required")
    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;

    /**
     * Order date and time
     */
    @NotNull(message = "Order date is required")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    /**
     * Order status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * Payment method
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 30)
    private PaymentMethod paymentMethod;

    /**
     * Payment status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    /**
     * Subtotal amount (sum of all items)
     */
    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", message = "Subtotal cannot be negative")
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    /**
     * Tax amount
     */
    @DecimalMin(value = "0.0", message = "Tax cannot be negative")
    @Column(name = "tax_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    /**
     * Shipping cost
     */
    @DecimalMin(value = "0.0", message = "Shipping cost cannot be negative")
    @Column(name = "shipping_cost", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal shippingCost = BigDecimal.ZERO;

    /**
     * Discount amount
     */
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * Total amount
     */
    @NotNull(message = "Total is required")
    @DecimalMin(value = "0.0", message = "Total cannot be negative")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Discount code applied
     */
    @Size(max = 50, message = "Discount code must not exceed 50 characters")
    @Column(name = "discount_code", length = 50)
    private String discountCode;

    /**
     * Customer notes
     */
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Tracking number for shipment
     */
    @Size(max = 100, message = "Tracking number must not exceed 100 characters")
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    /**
     * Shipping carrier
     */
    @Size(max = 100, message = "Carrier must not exceed 100 characters")
    @Column(name = "carrier", length = 100)
    private String carrier;

    /**
     * Expected delivery date
     */
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    /**
     * Actual delivery date
     */
    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;

    /**
     * Cancelled date
     */
    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    /**
     * Cancellation reason
     */
    @Size(max = 500, message = "Cancellation reason must not exceed 500 characters")
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    /**
     * Payment transaction ID
     */
    @Size(max = 100, message = "Transaction ID must not exceed 100 characters")
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    /**
     * User who placed the order
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Order items
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Shipping address (embedded or reference)
     */
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    /**
     * Billing address (embedded or reference)
     */
    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;

    /**
     * Calculate total amount based on subtotal, tax, shipping, and discount
     */
    public void calculateTotal() {
        this.totalAmount = subtotal
            .add(taxAmount != null ? taxAmount : BigDecimal.ZERO)
            .add(shippingCost != null ? shippingCost : BigDecimal.ZERO)
            .subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
    }

    /**
     * Calculate subtotal from order items
     */
    public void calculateSubtotal() {
        this.subtotal = items.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Add an order item
     * 
     * @param item the item to add
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Remove an order item
     * 
     * @param item the item to remove
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    /**
     * Get total number of items in the order
     * 
     * @return total item count
     */
    public int getTotalItemCount() {
        return items.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }

    /**
     * Check if order can be cancelled
     * 
     * @return true if order can be cancelled
     */
    public boolean isCancellable() {
        return status != null && status.isCancellable();
    }

    /**
     * Cancel the order
     * 
     * @param reason cancellation reason
     */
    public void cancel(String reason) {
        if (!isCancellable()) {
            throw new IllegalStateException("Order cannot be cancelled in current status: " + status);
        }
        this.status = OrderStatus.CANCELLED;
        this.cancelledDate = LocalDateTime.now();
        this.cancellationReason = reason;
    }

    /**
     * Mark order as delivered
     */
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredDate = LocalDateTime.now();
    }

    /**
     * Update order status
     * 
     * @param newStatus new status
     */
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        
        if (newStatus == OrderStatus.DELIVERED) {
            this.deliveredDate = LocalDateTime.now();
        } else if (newStatus == OrderStatus.CANCELLED) {
            this.cancelledDate = LocalDateTime.now();
        }
    }

    /**
     * Update payment status
     * 
     * @param newStatus new payment status
     */
    public void updatePaymentStatus(PaymentStatus newStatus) {
        this.paymentStatus = newStatus;
        
        if (newStatus == PaymentStatus.COMPLETED && this.status == OrderStatus.PENDING) {
            this.status = OrderStatus.CONFIRMED;
        }
    }
}
