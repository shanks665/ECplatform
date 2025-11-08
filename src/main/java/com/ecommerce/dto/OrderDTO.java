package com.ecommerce.dto;

import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.model.enums.PaymentMethod;
import com.ecommerce.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Order entity
 * 
 * @author E-Commerce Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String discountCode;
    private String trackingNumber;
    private String carrier;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime deliveredDate;
    private Long userId;
    private String userFullName;
    @Builder.Default
    private List<OrderItemDTO> items = new ArrayList<>();
    private String shippingAddress;
    private String billingAddress;
    private LocalDateTime createdAt;
    
    public int getTotalItemCount() {
        return items.stream()
            .mapToInt(OrderItemDTO::getQuantity)
            .sum();
    }
}
