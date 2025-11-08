package com.ecommerce.controller;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.model.ShoppingCart;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for shopping cart endpoints
 * 
 * @author E-Commerce Team
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Shopping Cart", description = "Shopping Cart API")
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "Get current user's cart")
    public ResponseEntity<ApiResponse<CartDTO>> getCart() {
        Long userId = authService.getCurrentUserId();
        CartDTO cart = cartService.getCartDTO(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<ApiResponse<CartDTO>> addItemToCart(@Valid @RequestBody AddToCartRequest request) {
        Long userId = authService.getCurrentUserId();
        ShoppingCart cart = cartService.addItemToCart(userId, request);
        CartDTO cartDTO = cartService.convertToDTO(cart);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartDTO));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<ApiResponse<CartDTO>> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        Long userId = authService.getCurrentUserId();
        ShoppingCart cart = cartService.updateCartItemQuantity(userId, itemId, quantity);
        CartDTO cartDTO = cartService.convertToDTO(cart);
        return ResponseEntity.ok(ApiResponse.success("Cart updated", cartDTO));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<CartDTO>> removeItemFromCart(@PathVariable Long itemId) {
        Long userId = authService.getCurrentUserId();
        ShoppingCart cart = cartService.removeItemFromCart(userId, itemId);
        CartDTO cartDTO = cartService.convertToDTO(cart);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cartDTO));
    }

    @DeleteMapping
    @Operation(summary = "Clear cart")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        Long userId = authService.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }
}
