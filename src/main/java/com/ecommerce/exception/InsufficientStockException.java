package com.ecommerce.exception;

/**
 * Exception thrown when product stock is insufficient
 * 
 * @author E-Commerce Team
 */
public class InsufficientStockException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for %s. Available: %d, Requested: %d", 
            productName, available, requested));
    }
}
