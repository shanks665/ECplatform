package com.ecommerce.controller;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.request.CreateOrderRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.PageResponse;
import com.ecommerce.model.Order;
import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.model.enums.PaymentStatus;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for order endpoints
 * 
 * @author E-Commerce Team
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Orders", description = "Order API")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long userId = authService.getCurrentUserId();
        Order order = orderService.createOrder(userId, request);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", dto));
    }

    @GetMapping
    @Operation(summary = "Get current user's orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Long userId = authService.getCurrentUserId();
        Sort sort = Sort.by("orderDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orderPage = orderService.getOrdersByUser(userId, pageable);
        Page<OrderDTO> dtoPage = orderPage.map(orderService::convertToDTO);
        
        PageResponse<OrderDTO> response = PageResponse.<OrderDTO>builder()
                .content(dtoPage.getContent())
                .pageNumber(dtoPage.getNumber())
                .pageSize(dtoPage.getSize())
                .totalElements(dtoPage.getTotalElements())
                .totalPages(dtoPage.getTotalPages())
                .first(dtoPage.isFirst())
                .last(dtoPage.isLast())
                .empty(dtoPage.isEmpty())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderByNumber(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<ApiResponse<OrderDTO>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason) {
        Order order = orderService.cancelOrder(orderId, reason);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled", dto));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Sort sort = Sort.by("orderDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orderPage = orderService.getAllOrders(pageable);
        Page<OrderDTO> dtoPage = orderPage.map(orderService::convertToDTO);
        
        PageResponse<OrderDTO> response = PageResponse.<OrderDTO>builder()
                .content(dtoPage.getContent())
                .pageNumber(dtoPage.getNumber())
                .pageSize(dtoPage.getSize())
                .totalElements(dtoPage.getTotalElements())
                .totalPages(dtoPage.getTotalPages())
                .first(dtoPage.isFirst())
                .last(dtoPage.isLast())
                .empty(dtoPage.isEmpty())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (Admin only)")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", dto));
    }

    @PatchMapping("/admin/{orderId}/payment-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update payment status (Admin only)")
    public ResponseEntity<ApiResponse<OrderDTO>> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestParam PaymentStatus paymentStatus) {
        Order order = orderService.updatePaymentStatus(orderId, paymentStatus);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.ok(ApiResponse.success("Payment status updated", dto));
    }

    @PatchMapping("/admin/{orderId}/tracking")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update tracking information (Admin only)")
    public ResponseEntity<ApiResponse<OrderDTO>> updateTrackingInfo(
            @PathVariable Long orderId,
            @RequestParam String trackingNumber,
            @RequestParam String carrier) {
        Order order = orderService.updateTrackingInfo(orderId, trackingNumber, carrier);
        OrderDTO dto = orderService.convertToDTO(order);
        return ResponseEntity.ok(ApiResponse.success("Tracking info updated", dto));
    }
}
