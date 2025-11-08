package com.ecommerce.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for price calculations
 * 
 * @author E-Commerce Team
 */
@Component
public class PriceCalculator {

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.10); // 10% tax
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = BigDecimal.valueOf(100.00);
    private static final BigDecimal STANDARD_SHIPPING = BigDecimal.valueOf(10.00);

    /**
     * Calculate tax amount
     * 
     * @param amount base amount
     * @return tax amount
     */
    public BigDecimal calculateTax(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate shipping cost
     * 
     * @param subtotal order subtotal
     * @return shipping cost
     */
    public BigDecimal calculateShipping(BigDecimal subtotal) {
        if (subtotal == null) {
            return STANDARD_SHIPPING;
        }
        
        if (subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        
        return STANDARD_SHIPPING;
    }

    /**
     * Calculate discount amount from percentage
     * 
     * @param amount base amount
     * @param discountPercentage discount percentage (0-100)
     * @return discount amount
     */
    public BigDecimal calculateDiscountAmount(BigDecimal amount, BigDecimal discountPercentage) {
        if (amount == null || discountPercentage == null || 
            amount.compareTo(BigDecimal.ZERO) <= 0 || 
            discountPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount = amount.multiply(discountPercentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return discount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate final price after discount
     * 
     * @param originalPrice original price
     * @param discountPercentage discount percentage
     * @return final price
     */
    public BigDecimal calculateFinalPrice(BigDecimal originalPrice, BigDecimal discountPercentage) {
        if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discountAmount = calculateDiscountAmount(originalPrice, discountPercentage);
        return originalPrice.subtract(discountAmount);
    }

    /**
     * Calculate profit margin
     * 
     * @param sellingPrice selling price
     * @param costPrice cost price
     * @return profit margin percentage
     */
    public BigDecimal calculateProfitMargin(BigDecimal sellingPrice, BigDecimal costPrice) {
        if (sellingPrice == null || costPrice == null || 
            sellingPrice.compareTo(BigDecimal.ZERO) <= 0 || 
            costPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal profit = sellingPrice.subtract(costPrice);
        BigDecimal margin = profit.divide(sellingPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        
        return margin.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Round price to 2 decimal places
     * 
     * @param price price to round
     * @return rounded price
     */
    public BigDecimal roundPrice(BigDecimal price) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Format price as string
     * 
     * @param price price to format
     * @return formatted price string
     */
    public String formatPrice(BigDecimal price) {
        if (price == null) {
            return "$0.00";
        }
        return String.format("$%.2f", price);
    }
}
