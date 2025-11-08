package com.ecommerce.repository;

import com.ecommerce.model.WishlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for WishlistItem entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    /**
     * Find wishlist items by user ID
     * 
     * @param userId user ID
     * @param pageable pagination information
     * @return page of wishlist items
     */
    Page<WishlistItem> findByUserId(Long userId, Pageable pageable);

    /**
     * Find wishlist item by user and product
     * 
     * @param userId user ID
     * @param productId product ID
     * @return Optional containing wishlist item if found
     */
    Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * Check if product is in user's wishlist
     * 
     * @param userId user ID
     * @param productId product ID
     * @return true if product is in wishlist
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    /**
     * Find items with sale notification enabled
     * 
     * @return list of items
     */
    @Query("SELECT w FROM WishlistItem w WHERE w.notifyOnSale = true")
    List<WishlistItem> findItemsWithSaleNotification();

    /**
     * Find items with restock notification enabled
     * 
     * @return list of items
     */
    @Query("SELECT w FROM WishlistItem w WHERE w.notifyOnRestock = true")
    List<WishlistItem> findItemsWithRestockNotification();

    /**
     * Find items where product is on sale
     * 
     * @param userId user ID
     * @return list of items
     */
    @Query("SELECT w FROM WishlistItem w WHERE w.user.id = :userId AND w.product.salePrice IS NOT NULL AND w.product.salePrice > 0 AND w.product.salePrice < w.product.price")
    List<WishlistItem> findItemsOnSale(@Param("userId") Long userId);

    /**
     * Count wishlist items by user
     * 
     * @param userId user ID
     * @return count of items
     */
    long countByUserId(Long userId);

    /**
     * Delete wishlist item by user and product
     * 
     * @param userId user ID
     * @param productId product ID
     */
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
