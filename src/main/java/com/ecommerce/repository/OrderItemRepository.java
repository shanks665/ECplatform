package com.ecommerce.repository;

import com.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OrderItem entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find order items by order ID
     * 
     * @param orderId order ID
     * @return list of order items
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Find order items by product ID
     * 
     * @param productId product ID
     * @return list of order items
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * Get total quantity sold for a product
     * 
     * @param productId product ID
     * @return total quantity sold
     */
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    Long getTotalQuantitySold(@Param("productId") Long productId);
}
