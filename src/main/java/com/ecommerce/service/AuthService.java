package com.ecommerce.service;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.request.LoginRequest;
import com.ecommerce.dto.request.RegisterRequest;
import com.ecommerce.dto.response.AuthResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication and authorization
 * 
 * @author E-Commerce Team
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    /**
     * Authenticate user and generate JWT token
     * 
     * @param loginRequest login credentials
     * @return authentication response with token
     */
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = tokenProvider.generateToken(authentication);

            // Get user details
            User user = (User) authentication.getPrincipal();
            
            // Update last login
            userService.updateLastLogin(user.getId());
            
            // Reset failed login attempts
            userService.resetFailedLoginAttempts(user.getId());

            UserDTO userDTO = userService.convertToDTO(user);

            return AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .user(userDTO)
                    .build();

        } catch (Exception e) {
            // Handle failed login attempt
            try {
                User user = userService.getUserByUsernameOrEmail(loginRequest.getUsernameOrEmail());
                userService.incrementFailedLoginAttempts(user);
            } catch (Exception ignored) {
                // User not found, ignore
            }
            
            throw new UnauthorizedException("Invalid username/email or password");
        }
    }

    /**
     * Register a new user
     * 
     * @param registerRequest registration details
     * @return authentication response with token
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        // Register user
        User user = userService.registerUser(registerRequest);

        // Auto-login after registration
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = tokenProvider.generateTokenFromUsername(user.getUsername());

        UserDTO userDTO = userService.convertToDTO(user);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userDTO)
                .build();
    }

    /**
     * Get current authenticated user
     * 
     * @return current user
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    /**
     * Get current user ID
     * 
     * @return current user ID
     */
    @Transactional(readOnly = true)
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Check if user is authenticated
     * 
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Logout user
     */
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
