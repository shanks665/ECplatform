package com.ecommerce.service;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.CartItemDTO;
import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.ShoppingCart;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.ShoppingCartRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for shopping cart management
 * 
 * @author E-Commerce Team
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Get or create shopping cart for user
     * 
     * @param userId user ID
     * @return shopping cart
     */
    public ShoppingCart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart cart = ShoppingCart.builder()
                            .user(user)
                            .lastActivity(LocalDateTime.now())
                            .build();
                    return cartRepository.save(cart);
                });
    }

    /**
     * Get cart by user ID
     * 
     * @param userId user ID
     * @return shopping cart
     */
    @Transactional(readOnly = true)
    public ShoppingCart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for user"));
    }

    /**
     * Add item to cart
     * 
     * @param userId user ID
     * @param request add to cart request
     * @return updated cart
     */
    public ShoppingCart addItemToCart(Long userId, AddToCartRequest request) {
        ShoppingCart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Check if product is available
        if (!product.isInStock()) {
            throw new BadRequestException("Product is out of stock");
        }

        // Check if sufficient stock available
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(
                    product.getName(),
                    product.getStockQuantity(),
                    request.getQuantity()
            );
        }

        // Check if item already exists in cart
        CartItem existingItem = cart.findItemByProductId(product.getId());

        if (existingItem != null) {
            // Update quantity
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new InsufficientStockException(
                        product.getName(),
                        product.getStockQuantity(),
                        newQuantity
                );
            }
            existingItem.updateQuantity(newQuantity);
        } else {
            // Add new item
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getEffectivePrice())
                    .build();
            cartItem.calculateTotalPrice();
            cart.addItem(cartItem);
        }

        cart.updateLastActivity();
        return cartRepository.save(cart);
    }

    /**
     * Update cart item quantity
     * 
     * @param userId user ID
     * @param cartItemId cart item ID
     * @param quantity new quantity
     * @return updated cart
     */
    public ShoppingCart updateCartItemQuantity(Long userId, Long cartItemId, int quantity) {
        ShoppingCart cart = getCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item", "id", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to user's cart");
        }

        if (quantity <= 0) {
            cart.removeItem(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < quantity) {
                throw new InsufficientStockException(
                        product.getName(),
                        product.getStockQuantity(),
                        quantity
                );
            }
            cartItem.updateQuantity(quantity);
        }

        cart.updateLastActivity();
        return cartRepository.save(cart);
    }

    /**
     * Remove item from cart
     * 
     * @param userId user ID
     * @param cartItemId cart item ID
     * @return updated cart
     */
    public ShoppingCart removeItemFromCart(Long userId, Long cartItemId) {
        ShoppingCart cart = getCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item", "id", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to user's cart");
        }

        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);

        cart.updateLastActivity();
        return cartRepository.save(cart);
    }

    /**
     * Clear all items from cart
     * 
     * @param userId user ID
     */
    public void clearCart(Long userId) {
        ShoppingCart cart = getCartByUserId(userId);
        cart.clear();
        cartItemRepository.deleteByCartId(cart.getId());
        cart.updateLastActivity();
        cartRepository.save(cart);
    }

    /**
     * Get cart DTO for user
     * 
     * @param userId user ID
     * @return cart DTO
     */
    @Transactional(readOnly = true)
    public CartDTO getCartDTO(Long userId) {
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        if (cart == null) {
            return CartDTO.builder()
                    .userId(userId)
                    .items(new ArrayList<>())
                    .build();
        }

        return convertToDTO(cart);
    }

    /**
     * Convert ShoppingCart to CartDTO
     * 
     * @param cart shopping cart
     * @return cart DTO
     */
    public CartDTO convertToDTO(ShoppingCart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser() != null ? cart.getUser().getId() : null)
                .items(itemDTOs)
                .lastActivity(cart.getLastActivity())
                .build();
    }

    /**
     * Convert CartItem to CartItemDTO
     * 
     * @param item cart item
     * @return cart item DTO
     */
    private CartItemDTO convertItemToDTO(CartItem item) {
        Product product = item.getProduct();
        return CartItemDTO.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .productImageUrl(product.getMainImageUrl())
                .productStockQuantity(product.getStockQuantity())
                .productInStock(product.isInStock())
                .build();
    }
}
