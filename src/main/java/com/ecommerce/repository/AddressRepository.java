package com.ecommerce.repository;

import com.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Address entity
 * 
 * @author E-Commerce Team
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Find addresses by user ID
     * 
     * @param userId user ID
     * @return list of addresses
     */
    List<Address> findByUserId(Long userId);

    /**
     * Find default address for user
     * 
     * @param userId user ID
     * @return Optional containing default address if found
     */
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    /**
     * Find addresses by user ID and type
     * 
     * @param userId user ID
     * @param addressType address type
     * @return list of addresses
     */
    List<Address> findByUserIdAndAddressType(Long userId, String addressType);

    /**
     * Set all addresses as non-default for user
     * 
     * @param userId user ID
     */
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void unsetDefaultForUser(@Param("userId") Long userId);

    /**
     * Count addresses by user
     * 
     * @param userId user ID
     * @return count of addresses
     */
    long countByUserId(Long userId);
}
