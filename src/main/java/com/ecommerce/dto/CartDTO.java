package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for ShoppingCart entity
 * 
 * @author E-Commerce Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    @Builder.Default
    private List<CartItemDTO> items = new ArrayList<>();
    private LocalDateTime lastActivity;
    
    public int getTotalItemCount() {
        return items.stream()
            .mapToInt(CartItemDTO::getQuantity)
            .sum();
    }
    
    public BigDecimal getSubtotal() {
        return items.stream()
            .map(CartItemDTO::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
