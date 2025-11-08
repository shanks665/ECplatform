package com.ecommerce.controller;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user endpoints
 * 
 * @author E-Commerce Team
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "User API")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        User user = authService.getCurrentUser();
        UserDTO dto = userService.convertToDTO(user);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@Valid @RequestBody UserDTO userDTO) {
        Long userId = authService.getCurrentUserId();
        User user = userService.updateUserProfile(userId, userDTO);
        UserDTO dto = userService.convertToDTO(user);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", dto));
    }

    @PutMapping("/me/password")
    @Operation(summary = "Change password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestParam String newPassword) {
        Long userId = authService.getCurrentUserId();
        userService.changePassword(userId, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO dto = userService.convertToDTO(user);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
