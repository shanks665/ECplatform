package com.ecommerce.exception;

/**
 * Exception thrown for unauthorized access attempts
 * 
 * @author E-Commerce Team
 */
public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public UnauthorizedException(String message) {
        super(message);
    }
}
