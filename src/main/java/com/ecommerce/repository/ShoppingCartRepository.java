package com.ecommerce.repository;

import com.ecommerce.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ShoppingCart entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    /**
     * Find shopping cart by user ID
     * 
     * @param userId user ID
     * @return Optional containing cart if found
     */
    Optional<ShoppingCart> findByUserId(Long userId);

    /**
     * Find shopping cart by session ID
     * 
     * @param sessionId session ID
     * @return Optional containing cart if found
     */
    Optional<ShoppingCart> findBySessionId(String sessionId);

    /**
     * Find abandoned carts
     * 
     * @param date threshold date
     * @return list of abandoned carts
     */
    @Query("SELECT c FROM ShoppingCart c WHERE c.lastActivity < :date AND SIZE(c.items) > 0")
    List<ShoppingCart> findAbandonedCarts(@Param("date") LocalDateTime date);

    /**
     * Delete empty carts older than date
     * 
     * @param date threshold date
     */
    @Modifying
    @Query("DELETE FROM ShoppingCart c WHERE SIZE(c.items) = 0 AND c.lastActivity < :date")
    void deleteEmptyCartsOlderThan(@Param("date") LocalDateTime date);

    /**
     * Count active carts
     * 
     * @return count of active carts
     */
    @Query("SELECT COUNT(c) FROM ShoppingCart c WHERE SIZE(c.items) > 0")
    long countActiveCarts();
}
