package com.ecommerce.model.enums;

/**
 * User role enumeration
 * Defines different roles a user can have in the system
 * 
 * @author E-Commerce Team
 */
public enum UserRole {
    /**
     * Regular customer role
     */
    CUSTOMER("Customer", "Regular customer with standard privileges"),
    
    /**
     * Seller role - can manage products
     */
    SELLER("Seller", "Can list and manage products for sale"),
    
    /**
     * Administrator role - full system access
     */
    ADMIN("Administrator", "Full system access and management");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
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
     * Check if role has admin privileges
     * 
     * @return true if role is ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Check if role has seller privileges
     * 
     * @return true if role is SELLER or ADMIN
     */
    public boolean canSell() {
        return this == SELLER || this == ADMIN;
    }
}
