package com.ecommerce.controller;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.request.CreateProductRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.PageResponse;
import com.ecommerce.model.Product;
import com.ecommerce.model.enums.ProductStatus;
import com.ecommerce.service.ProductService;
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
 * REST controller for product endpoints
 * Handles product management and retrieval
 * 
 * @author E-Commerce Team
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product API")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Product> productPage = productService.getActiveProducts(pageable);
        Page<ProductDTO> dtoPage = productPage.map(productService::convertToDTO);
        
        PageResponse<ProductDTO> response = PageResponse.<ProductDTO>builder()
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

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        productService.incrementViewCount(id);
        ProductDTO dto = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get product by slug")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductBySlug(@PathVariable String slug) {
        Product product = productService.getProductBySlug(slug);
        productService.incrementViewCount(product.getId());
        ProductDTO dto = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProducts(keyword, pageable);
        Page<ProductDTO> dtoPage = productPage.map(productService::convertToDTO);
        
        PageResponse<ProductDTO> response = PageResponse.<ProductDTO>builder()
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

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductsByCategory(categoryId, pageable);
        Page<ProductDTO> dtoPage = productPage.map(productService::convertToDTO);
        
        PageResponse<ProductDTO> response = PageResponse.<ProductDTO>builder()
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

    @GetMapping("/featured")
    @Operation(summary = "Get featured products")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getFeaturedProducts(pageable);
        Page<ProductDTO> dtoPage = productPage.map(productService::convertToDTO);
        
        PageResponse<ProductDTO> response = PageResponse.<ProductDTO>builder()
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

    @GetMapping("/on-sale")
    @Operation(summary = "Get products on sale")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getProductsOnSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductsOnSale(pageable);
        Page<ProductDTO> dtoPage = productPage.map(productService::convertToDTO);
        
        PageResponse<ProductDTO> response = PageResponse.<ProductDTO>builder()
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

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new product")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);
        ProductDTO dto = productService.convertToDTO(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {
        Product product = productService.updateProduct(id, request);
        ProductDTO dto = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update product status")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status) {
        Product product = productService.updateProductStatus(id, status);
        ProductDTO dto = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success("Product status updated", dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete product")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
