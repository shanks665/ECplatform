package com.ecommerce.repository;

import com.ecommerce.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity
 * Provides database operations for category management
 * 
 * @author E-Commerce Team
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by slug
     * 
     * @param slug category slug
     * @return Optional containing category if found
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Check if slug exists
     * 
     * @param slug slug to check
     * @return true if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find active categories
     * 
     * @param pageable pagination information
     * @return page of active categories
     */
    Page<Category> findByActiveTrue(Pageable pageable);

    /**
     * Find root categories (no parent)
     * 
     * @return list of root categories
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.deleted = false ORDER BY c.displayOrder, c.name")
    List<Category> findRootCategories();

    /**
     * Find active root categories
     * 
     * @return list of active root categories
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.active = true AND c.deleted = false ORDER BY c.displayOrder, c.name")
    List<Category> findActiveRootCategories();

    /**
     * Find child categories
     * 
     * @param parentId parent category ID
     * @return list of child categories
     */
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.deleted = false ORDER BY c.displayOrder, c.name")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    /**
     * Find active child categories
     * 
     * @param parentId parent category ID
     * @return list of active child categories
     */
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.active = true AND c.deleted = false ORDER BY c.displayOrder, c.name")
    List<Category> findActiveByParentId(@Param("parentId") Long parentId);

    /**
     * Search categories by name
     * 
     * @param keyword search keyword
     * @param pageable pagination information
     * @return page of categories
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND c.deleted = false")
    Page<Category> searchByName(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Count categories by parent
     * 
     * @param parentId parent category ID
     * @return count of child categories
     */
    long countByParentId(Long parentId);

    /**
     * Count products in category
     * 
     * @param categoryId category ID
     * @return count of products
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId AND p.deleted = false")
    long countProductsInCategory(@Param("categoryId") Long categoryId);

    /**
     * Find categories with products
     * 
     * @return list of categories with products
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p WHERE p.status = 'ACTIVE' AND c.active = true AND c.deleted = false")
    List<Category> findCategoriesWithProducts();
}
