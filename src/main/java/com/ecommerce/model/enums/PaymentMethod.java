package com.ecommerce.model.enums;

/**
 * Payment method enumeration
 * Supported payment methods in the system
 * 
 * @author E-Commerce Team
 */
public enum PaymentMethod {
    /**
     * Credit card payment
     */
    CREDIT_CARD("Credit Card", "Payment via credit card"),
    
    /**
     * Debit card payment
     */
    DEBIT_CARD("Debit Card", "Payment via debit card"),
    
    /**
     * PayPal payment
     */
    PAYPAL("PayPal", "Payment via PayPal"),
    
    /**
     * Bank transfer
     */
    BANK_TRANSFER("Bank Transfer", "Direct bank transfer"),
    
    /**
     * Cash on delivery
     */
    CASH_ON_DELIVERY("Cash on Delivery", "Pay when order is delivered"),
    
    /**
     * Digital wallet
     */
    WALLET("Digital Wallet", "Payment via digital wallet");

    private final String displayName;
    private final String description;

    PaymentMethod(String displayName, String description) {
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
     * Check if payment method requires immediate payment
     * 
     * @return true if immediate payment required
     */
    public boolean requiresImmediatePayment() {
        return this != CASH_ON_DELIVERY;
    }
}
