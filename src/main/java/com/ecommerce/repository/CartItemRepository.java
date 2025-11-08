package com.ecommerce.repository;

import com.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Find cart items by cart ID
     * 
     * @param cartId cart ID
     * @return list of cart items
     */
    List<CartItem> findByCartId(Long cartId);

    /**
     * Find cart item by cart ID and product ID
     * 
     * @param cartId cart ID
     * @param productId product ID
     * @return Optional containing cart item if found
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    /**
     * Delete cart items by cart ID
     * 
     * @param cartId cart ID
     */
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    /**
     * Count items in cart
     * 
     * @param cartId cart ID
     * @return count of items
     */
    long countByCartId(Long cartId);
}
