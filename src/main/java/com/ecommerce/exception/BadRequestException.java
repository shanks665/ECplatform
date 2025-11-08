package com.ecommerce.exception;

/**
 * Exception thrown for bad requests
 * 
 * @author E-Commerce Team
 */
public class BadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public BadRequestException(String message) {
        super(message);
    }
}
