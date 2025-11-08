package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Address entity representing shipping and billing addresses
 * Associated with users for delivery purposes
 * 
 * @author E-Commerce Team
 */
@Entity
@Table(name = "addresses", indexes = {
    @Index(name = "idx_address_user", columnList = "user_id"),
    @Index(name = "idx_address_type", columnList = "address_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@ToString(exclude = {"user"})
public class Address extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Address type (SHIPPING, BILLING, BOTH)
     */
    @NotBlank(message = "Address type is required")
    @Column(name = "address_type", nullable = false, length = 20)
    private String addressType;

    /**
     * Full name for this address
     */
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    /**
     * Phone number
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    /**
     * Street address line 1
     */
    @NotBlank(message = "Street address is required")
    @Size(max = 200, message = "Street address must not exceed 200 characters")
    @Column(name = "street_address", nullable = false, length = 200)
    private String streetAddress;

    /**
     * Street address line 2 (optional)
     */
    @Size(max = 200, message = "Address line 2 must not exceed 200 characters")
    @Column(name = "address_line2", length = 200)
    private String addressLine2;

    /**
     * City
     */
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    /**
     * State or province
     */
    @NotBlank(message = "State/Province is required")
    @Size(max = 100, message = "State/Province must not exceed 100 characters")
    @Column(name = "state", nullable = false, length = 100)
    private String state;

    /**
     * Postal or ZIP code
     */
    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    /**
     * Country
     */
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    /**
     * Whether this is the default address
     */
    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    /**
     * Additional delivery instructions
     */
    @Size(max = 500, message = "Delivery instructions must not exceed 500 characters")
    @Column(name = "delivery_instructions", length = 500)
    private String deliveryInstructions;

    /**
     * Associated user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Get full address as a formatted string
     * 
     * @return formatted address
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(streetAddress);
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            sb.append(", ").append(addressLine2);
        }
        
        sb.append(", ").append(city);
        sb.append(", ").append(state);
        sb.append(" ").append(postalCode);
        sb.append(", ").append(country);
        
        return sb.toString();
    }

    /**
     * Get address for shipping label
     * 
     * @return shipping label address
     */
    public String getShippingLabel() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullName).append("\n");
        sb.append(phoneNumber).append("\n");
        sb.append(streetAddress).append("\n");
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            sb.append(addressLine2).append("\n");
        }
        
        sb.append(city).append(", ").append(state).append(" ").append(postalCode).append("\n");
        sb.append(country);
        
        return sb.toString();
    }
}
