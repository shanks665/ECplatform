package com.ecommerce.model.enums;

/**
 * User account status enumeration
 * Represents the current state of a user account
 * 
 * @author E-Commerce Team
 */
public enum UserStatus {
    /**
     * Account pending email verification
     */
    PENDING("Pending", "Account created but email not verified"),
    
    /**
     * Active account
     */
    ACTIVE("Active", "Account is active and in good standing"),
    
    /**
     * Suspended account
     */
    SUSPENDED("Suspended", "Account temporarily suspended"),
    
    /**
     * Banned account
     */
    BANNED("Banned", "Account permanently banned"),
    
    /**
     * Inactive account
     */
    INACTIVE("Inactive", "Account deactivated by user");

    private final String displayName;
    private final String description;

    UserStatus(String displayName, String description) {
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
     * Check if user can login
     * 
     * @return true if status allows login
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }

    /**
     * Check if account is restricted
     * 
     * @return true if account is suspended or banned
     */
    public boolean isRestricted() {
        return this == SUSPENDED || this == BANNED;
    }
}
