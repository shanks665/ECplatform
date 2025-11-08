package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * ProductImage entity representing images associated with a product
 * Supports multiple images per product with ordering
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "product_images", indexes = {
    @Index(name = "idx_product_image_product", columnList = "product_id"),
    @Index(name = "idx_product_image_order", columnList = "display_order")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@ToString(exclude = {"product"})
public class ProductImage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Image URL
     */
    @NotBlank(message = "Image URL is required")
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    /**
     * Thumbnail URL
     */
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    /**
     * Alt text for accessibility
     */
    @Size(max = 200, message = "Alt text must not exceed 200 characters")
    @Column(name = "alt_text", length = 200)
    private String altText;

    /**
     * Display order
     */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    /**
     * Whether this is the primary image
     */
    @Column(name = "is_primary")
    @Builder.Default
    private Boolean isPrimary = false;

    /**
     * Associated product
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Image file size in bytes
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * Image width in pixels
     */
    @Column(name = "width")
    private Integer width;

    /**
     * Image height in pixels
     */
    @Column(name = "height")
    private Integer height;

    /**
     * MIME type of the image
     */
    @Column(name = "mime_type", length = 50)
    private String mimeType;
}
