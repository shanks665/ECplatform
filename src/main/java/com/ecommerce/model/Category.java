package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Category entity representing product categories
 * Supports hierarchical category structure with parent-child relationships
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "categories", indexes = {
    @Index(name = "idx_category_name", columnList = "name"),
    @Index(name = "idx_category_slug", columnList = "slug"),
    @Index(name = "idx_category_parent", columnList = "parent_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children", "products"})
@ToString(exclude = {"parent", "children", "products"})
public class Category extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Category name
     */
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * URL-friendly slug for the category
     */
    @NotBlank(message = "Category slug is required")
    @Size(max = 100, message = "Category slug must not exceed 100 characters")
    @Column(name = "slug", unique = true, nullable = false, length = 100)
    private String slug;

    /**
     * Category description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Category icon or image URL
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * Display order for sorting
     */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    /**
     * Whether category is active
     */
    @Column(name = "active")
    @Builder.Default
    private Boolean active = true;

    /**
     * Parent category for hierarchical structure
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    /**
     * Child categories
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    /**
     * Products in this category
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    /**
     * Meta title for SEO
     */
    @Column(name = "meta_title", length = 200)
    private String metaTitle;

    /**
     * Meta description for SEO
     */
    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    /**
     * Meta keywords for SEO
     */
    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    /**
     * Add a child category
     * 
     * @param child the child category to add
     */
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Remove a child category
     * 
     * @param child the child category to remove
     */
    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * Add a product to this category
     * 
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    /**
     * Check if this is a root category
     * 
     * @return true if category has no parent
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Check if this is a leaf category
     * 
     * @return true if category has no children
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    /**
     * Get the full path of the category
     * 
     * @return full category path
     */
    public String getFullPath() {
        if (parent == null) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }
}
