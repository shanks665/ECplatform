package com.ecommerce.service;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.request.RegisterRequest;
import com.ecommerce.dto.response.PageResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;
import com.ecommerce.model.enums.UserStatus;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for user management operations
 * 
 * @author E-Commerce Team
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * 
     * @param request registration request
     * @return created user
     */
    public User registerUser(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(UserRole.CUSTOMER)
                .status(UserStatus.PENDING)
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .build();

        return userRepository.save(user);
    }

    /**
     * Get user by ID
     * 
     * @param userId user ID
     * @return user
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    /**
     * Get user by username
     * 
     * @param username username
     * @return user
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Get user by email
     * 
     * @param email email
     * @return user
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Get user by username or email
     * 
     * @param usernameOrEmail username or email
     * @return user
     */
    @Transactional(readOnly = true)
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Update user profile
     * 
     * @param userId user ID
     * @param userDTO user data
     * @return updated user
     */
    public User updateUserProfile(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getProfileImageUrl() != null) {
            user.setProfileImageUrl(userDTO.getProfileImageUrl());
        }

        return userRepository.save(user);
    }

    /**
     * Change user password
     * 
     * @param userId user ID
     * @param newPassword new password
     */
    public void changePassword(Long userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Verify user email
     * 
     * @param token verification token
     * @return verified user
     */
    public User verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerificationToken(null);

        return userRepository.save(user);
    }

    /**
     * Update user last login
     * 
     * @param userId user ID
     */
    public void updateLastLogin(Long userId) {
        userRepository.updateLastLogin(userId, LocalDateTime.now());
    }

    /**
     * Increment failed login attempts
     * 
     * @param user user
     */
    public void incrementFailedLoginAttempts(User user) {
        user.incrementFailedLoginAttempts();
        userRepository.save(user);
    }

    /**
     * Reset failed login attempts
     * 
     * @param userId user ID
     */
    public void resetFailedLoginAttempts(Long userId) {
        userRepository.resetFailedLoginAttempts(userId);
    }

    /**
     * Get all users with pagination
     * 
     * @param pageable pagination information
     * @return page of users
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Search users
     * 
     * @param keyword search keyword
     * @param pageable pagination information
     * @return page of users
     */
    @Transactional(readOnly = true)
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchUsers(keyword, pageable);
    }

    /**
     * Delete user (soft delete)
     * 
     * @param userId user ID
     */
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        user.softDelete();
        userRepository.save(user);
    }

    /**
     * Update user status
     * 
     * @param userId user ID
     * @param status new status
     * @return updated user
     */
    public User updateUserStatus(Long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        return userRepository.save(user);
    }

    /**
     * Update user role
     * 
     * @param userId user ID
     * @param role new role
     * @return updated user
     */
    public User updateUserRole(Long userId, UserRole role) {
        User user = getUserById(userId);
        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * Convert User entity to UserDTO
     * 
     * @param user user entity
     * @return user DTO
     */
    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .profileImageUrl(user.getProfileImageUrl())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
