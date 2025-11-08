package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.dto.request.CreateOrderRequest;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.*;
import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.model.enums.PaymentStatus;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for order management
 * 
 * @author E-Commerce Team
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository cartRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    /**
     * Create order from cart
     * 
     * @param userId user ID
     * @param request create order request
     * @return created order
     */
    public Order createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Shopping cart is empty"));

        if (cart.isEmpty()) {
            throw new BadRequestException("Cannot create order from empty cart");
        }

        // Get addresses
        Address shippingAddress = addressRepository.findById(request.getShippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address", "id", request.getShippingAddressId()));

        Address billingAddress = addressRepository.findById(request.getBillingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Billing address", "id", request.getBillingAddressId()));

        // Generate order number
        String orderNumber = generateOrderNumber();

        // Calculate totals
        BigDecimal subtotal = cart.getSubtotal();
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(0.1)); // 10% tax
        BigDecimal shippingCost = calculateShippingCost(subtotal);
        BigDecimal discountAmount = BigDecimal.ZERO; // TODO: Apply discount code
        BigDecimal totalAmount = subtotal.add(taxAmount).add(shippingCost).subtract(discountAmount);

        // Create order
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .orderDate(LocalDateTime.now())
                .user(user)
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .subtotal(subtotal)
                .taxAmount(taxAmount)
                .shippingCost(shippingCost)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .discountCode(request.getDiscountCode())
                .notes(request.getNotes())
                .shippingAddress(shippingAddress.getShippingLabel())
                .billingAddress(billingAddress.getShippingLabel())
                .build();

        // Create order items from cart items
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Check stock availability
            if (!product.isInStock() || product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .productName(product.getName())
                    .productSku(product.getSku())
                    .build();
            orderItem.calculateTotalPrice();

            order.addItem(orderItem);

            // Reduce stock
            product.removeStock(cartItem.getQuantity());
            product.incrementSalesCount(cartItem.getQuantity());
            productRepository.save(product);
        }

        // Save order
        order = orderRepository.save(order);

        // Clear cart
        cart.clear();
        cartRepository.save(cart);

        return order;
    }

    /**
     * Get order by ID
     * 
     * @param orderId order ID
     * @return order
     */
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    }

    /**
     * Get order by order number
     * 
     * @param orderNumber order number
     * @return order
     */
    @Transactional(readOnly = true)
    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
    }

    /**
     * Get orders for user
     * 
     * @param userId user ID
     * @param pageable pagination information
     * @return page of orders
     */
    @Transactional(readOnly = true)
    public Page<Order> getOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    /**
     * Get all orders
     * 
     * @param pageable pagination information
     * @return page of orders
     */
    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * Update order status
     * 
     * @param orderId order ID
     * @param status new status
     * @return updated order
     */
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.updateStatus(status);
        return orderRepository.save(order);
    }

    /**
     * Update payment status
     * 
     * @param orderId order ID
     * @param paymentStatus new payment status
     * @return updated order
     */
    public Order updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
        Order order = getOrderById(orderId);
        order.updatePaymentStatus(paymentStatus);
        return orderRepository.save(order);
    }

    /**
     * Cancel order
     * 
     * @param orderId order ID
     * @param reason cancellation reason
     * @return cancelled order
     */
    public Order cancelOrder(Long orderId, String reason) {
        Order order = getOrderById(orderId);

        if (!order.isCancellable()) {
            throw new BadRequestException("Order cannot be cancelled in current status");
        }

        // Restore stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.addStock(item.getQuantity());
            productRepository.save(product);
        }

        order.cancel(reason);
        return orderRepository.save(order);
    }

    /**
     * Update tracking information
     * 
     * @param orderId order ID
     * @param trackingNumber tracking number
     * @param carrier carrier name
     * @return updated order
     */
    public Order updateTrackingInfo(Long orderId, String trackingNumber, String carrier) {
        Order order = getOrderById(orderId);
        order.setTrackingNumber(trackingNumber);
        order.setCarrier(carrier);
        order.updateStatus(OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }

    /**
     * Generate unique order number
     * 
     * @return order number
     */
    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return "ORD-" + timestamp + "-" + (int) (Math.random() * 1000);
    }

    /**
     * Calculate shipping cost based on subtotal
     * 
     * @param subtotal subtotal amount
     * @return shipping cost
     */
    private BigDecimal calculateShippingCost(BigDecimal subtotal) {
        // Free shipping for orders over 100
        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(10);
    }

    /**
     * Convert Order to OrderDTO
     * 
     * @param order order entity
     * @return order DTO
     */
    public OrderDTO convertToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .subtotal(order.getSubtotal())
                .taxAmount(order.getTaxAmount())
                .shippingCost(order.getShippingCost())
                .discountAmount(order.getDiscountAmount())
                .totalAmount(order.getTotalAmount())
                .discountCode(order.getDiscountCode())
                .trackingNumber(order.getTrackingNumber())
                .carrier(order.getCarrier())
                .expectedDeliveryDate(order.getExpectedDeliveryDate())
                .deliveredDate(order.getDeliveredDate())
                .userId(order.getUser().getId())
                .userFullName(order.getUser().getFullName())
                .items(itemDTOs)
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .createdAt(order.getCreatedAt())
                .build();
    }

    /**
     * Convert OrderItem to OrderItemDTO
     * 
     * @param item order item
     * @return order item DTO
     */
    private OrderItemDTO convertItemToDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .discount(item.getDiscount())
                .tax(item.getTax())
                .productName(item.getProductName())
                .productSku(item.getProductSku())
                .productId(item.getProduct().getId())
                .productImageUrl(item.getProduct().getMainImageUrl())
                .build();
    }
}
