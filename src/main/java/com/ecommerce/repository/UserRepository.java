package com.ecommerce.repository;

import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;
import com.ecommerce.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * Provides database operations for user management
 * 
 * @author E-Commerce Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Find user by username
     * 
     * @param username username to search
     * @return Optional containing user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     * 
     * @param email email to search
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username or email
     * 
     * @param username username to search
     * @param email email to search
     * @return Optional containing user if found
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Find user by verification token
     * 
     * @param token verification token
     * @return Optional containing user if found
     */
    Optional<User> findByVerificationToken(String token);

    /**
     * Find user by reset token
     * 
     * @param token reset token
     * @return Optional containing user if found
     */
    Optional<User> findByResetToken(String token);

    /**
     * Check if username exists
     * 
     * @param username username to check
     * @return true if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     * 
     * @param email email to check
     * @return true if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find users by role
     * 
     * @param role user role
     * @param pageable pagination information
     * @return page of users
     */
    Page<User> findByRole(UserRole role, Pageable pageable);

    /**
     * Find users by status
     * 
     * @param status user status
     * @param pageable pagination information
     * @return page of users
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * Find users by role and status
     * 
     * @param role user role
     * @param status user status
     * @param pageable pagination information
     * @return page of users
     */
    Page<User> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);

    /**
     * Find users created after a specific date
     * 
     * @param date date threshold
     * @return list of users
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find active users
     * 
     * @param pageable pagination information
     * @return page of active users
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.deleted = false")
    Page<User> findActiveUsers(Pageable pageable);

    /**
     * Find users with unverified email
     * 
     * @return list of users
     */
    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.deleted = false")
    List<User> findUsersWithUnverifiedEmail();

    /**
     * Find locked users
     * 
     * @return list of locked users
     */
    @Query("SELECT u FROM User u WHERE u.lockedUntil IS NOT NULL AND u.lockedUntil > CURRENT_TIMESTAMP")
    List<User> findLockedUsers();

    /**
     * Search users by keyword
     * 
     * @param keyword search keyword
     * @param pageable pagination information
     * @return page of users
     */
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "u.deleted = false")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Count users by role
     * 
     * @param role user role
     * @return count of users
     */
    long countByRole(UserRole role);

    /**
     * Count users by status
     * 
     * @param status user status
     * @return count of users
     */
    long countByStatus(UserStatus status);

    /**
     * Update user last login
     * 
     * @param userId user ID
     * @param lastLogin last login timestamp
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);

    /**
     * Reset failed login attempts
     * 
     * @param userId user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0, u.lockedUntil = null WHERE u.id = :userId")
    void resetFailedLoginAttempts(@Param("userId") Long userId);
}
