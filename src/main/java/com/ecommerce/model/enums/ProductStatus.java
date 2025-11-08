package com.ecommerce.model.enums;

/**
 * Product status enumeration
 * Represents the availability status of a product
 * 
 * @author E-Commerce Team
 */
public enum ProductStatus {
    /**
     * Product is in draft state
     */
    DRAFT("Draft", "Product is being created"),
    
    /**
     * Product is active and available for purchase
     */
    ACTIVE("Active", "Product is available for purchase"),
    
    /**
     * Product is temporarily out of stock
     */
    OUT_OF_STOCK("Out of Stock", "Product is temporarily unavailable"),
    
    /**
     * Product is discontinued
     */
    DISCONTINUED("Discontinued", "Product is no longer available"),
    
    /**
     * Product is archived
     */
    ARCHIVED("Archived", "Product is archived");

    private final String displayName;
    private final String description;

    ProductStatus(String displayName, String description) {
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
     * Check if product is available for purchase
     * 
     * @return true if product can be purchased
     */
    public boolean isAvailable() {
        return this == ACTIVE;
    }

    /**
     * Check if product is visible to customers
     * 
     * @return true if product should be shown
     */
    public boolean isVisible() {
        return this == ACTIVE || this == OUT_OF_STOCK;
    }
}
