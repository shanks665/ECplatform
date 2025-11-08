package com.ecommerce.model.enums;

/**
 * Payment status enumeration
 * Represents the status of a payment transaction
 * 
 * @author E-Commerce Team
 */
public enum PaymentStatus {
    /**
     * Payment pending
     */
    PENDING("Pending", "Payment is pending"),
    
    /**
     * Payment processing
     */
    PROCESSING("Processing", "Payment is being processed"),
    
    /**
     * Payment completed successfully
     */
    COMPLETED("Completed", "Payment completed successfully"),
    
    /**
     * Payment failed
     */
    FAILED("Failed", "Payment failed"),
    
    /**
     * Payment refunded
     */
    REFUNDED("Refunded", "Payment has been refunded"),
    
    /**
     * Payment cancelled
     */
    CANCELLED("Cancelled", "Payment was cancelled");

    private final String displayName;
    private final String description;

    PaymentStatus(String displayName, String description) {
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
     * Check if payment is successful
     * 
     * @return true if payment is completed
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }

    /**
     * Check if payment is in final state
     * 
     * @return true if payment cannot change state
     */
    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED || this == REFUNDED;
    }
}
