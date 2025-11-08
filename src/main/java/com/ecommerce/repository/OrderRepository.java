package com.ecommerce.repository;

import com.ecommerce.model.Order;
import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.model.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity
 * Provides database operations for order management
 * 
 * @author E-Commerce Team
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * Find order by order number
     * 
     * @param orderNumber order number
     * @return Optional containing order if found
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find orders by user ID
     * 
     * @param userId user ID
     * @param pageable pagination information
     * @return page of orders
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Find orders by user ID and status
     * 
     * @param userId user ID
     * @param status order status
     * @param pageable pagination information
     * @return page of orders
     */
    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    /**
     * Find orders by status
     * 
     * @param status order status
     * @param pageable pagination information
     * @return page of orders
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find orders by payment status
     * 
     * @param paymentStatus payment status
     * @param pageable pagination information
     * @return page of orders
     */
    Page<Order> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);

    /**
     * Find orders placed between dates
     * 
     * @param startDate start date
     * @param endDate end date
     * @param pageable pagination information
     * @return page of orders
     */
    Page<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find pending orders older than specified time
     * 
     * @param date threshold date
     * @return list of pending orders
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.orderDate < :date")
    List<Order> findPendingOrdersOlderThan(@Param("date") LocalDateTime date);

    /**
     * Find orders by tracking number
     * 
     * @param trackingNumber tracking number
     * @return Optional containing order if found
     */
    Optional<Order> findByTrackingNumber(String trackingNumber);

    /**
     * Search orders by keyword
     * 
     * @param keyword search keyword
     * @param pageable pagination information
     * @return page of orders
     */
    @Query("SELECT o FROM Order o WHERE " +
           "(LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.trackingNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "o.deleted = false")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Calculate total revenue
     * 
     * @return total revenue
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.paymentStatus = 'COMPLETED'")
    BigDecimal calculateTotalRevenue();

    /**
     * Calculate revenue for date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return revenue for period
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.paymentStatus = 'COMPLETED' AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueForPeriod(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Count orders by user
     * 
     * @param userId user ID
     * @return count of orders
     */
    long countByUserId(Long userId);

    /**
     * Count orders by status
     * 
     * @param status order status
     * @return count of orders
     */
    long countByStatus(OrderStatus status);

    /**
     * Get orders requiring attention (pending payment, processing issues)
     * 
     * @param pageable pagination information
     * @return page of orders
     */
    @Query("SELECT o FROM Order o WHERE " +
           "(o.status = 'PENDING' AND o.orderDate < :threshold) OR " +
           "(o.paymentStatus = 'FAILED') " +
           "ORDER BY o.orderDate ASC")
    Page<Order> findOrdersRequiringAttention(@Param("threshold") LocalDateTime threshold, Pageable pageable);

    /**
     * Find recent orders for user
     * 
     * @param userId user ID
     * @param limit number of orders to return
     * @return list of recent orders
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.orderDate DESC")
    List<Order> findRecentOrdersByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Get average order value
     * 
     * @return average order value
     */
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.paymentStatus = 'COMPLETED'")
    BigDecimal getAverageOrderValue();
}
