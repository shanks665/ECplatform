package com.ecommerce.model.enums;

/**
 * Order status enumeration
 * Represents different states of an order in its lifecycle
 * 
 * @author E-Commerce Team
 */
public enum OrderStatus {
    /**
     * Order created but payment pending
     */
    PENDING("Pending", "Order created, awaiting payment"),
    
    /**
     * Payment confirmed
     */
    CONFIRMED("Confirmed", "Payment confirmed, preparing for shipment"),
    
    /**
     * Order is being processed
     */
    PROCESSING("Processing", "Order is being processed"),
    
    /**
     * Order has been shipped
     */
    SHIPPED("Shipped", "Order has been shipped"),
    
    /**
     * Order is out for delivery
     */
    OUT_FOR_DELIVERY("Out for Delivery", "Order is out for delivery"),
    
    /**
     * Order delivered successfully
     */
    DELIVERED("Delivered", "Order delivered successfully"),
    
    /**
     * Order cancelled
     */
    CANCELLED("Cancelled", "Order has been cancelled"),
    
    /**
     * Order returned by customer
     */
    RETURNED("Returned", "Order has been returned"),
    
    /**
     * Order refunded
     */
    REFUNDED("Refunded", "Payment has been refunded");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if order can be cancelled
     * 
     * @return true if order can be cancelled
     */
    public boolean isCancellable() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING;
    }

    /**
     * Check if order is in final state
     * 
     * @return true if order is in a final state
     */
    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED || this == REFUNDED;
    }

    /**
     * Check if order can be modified
     * 
     * @return true if order can be modified
     */
    public boolean isModifiable() {
        return this == PENDING;
    }
}
