package com.ecommerce.util;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Email service for sending notifications
 * 
 * @author E-Commerce Team
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@ecommerce.com}")
    private String fromEmail;

    /**
     * Send email verification message
     * 
     * @param user user to verify
     * @param token verification token
     */
    @Async
    public void sendVerificationEmail(User user, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Verify Your Email - E-Commerce Platform");
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "Thank you for registering with E-Commerce Platform.\n\n" +
                    "Please verify your email by clicking the link below:\n" +
                    "http://localhost:8080/api/auth/verify-email?token=%s\n\n" +
                    "If you did not create this account, please ignore this email.\n\n" +
                    "Best regards,\n" +
                    "E-Commerce Team",
                    user.getFullName(), token
            ));

            mailSender.send(message);
            logger.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send verification email to: {}", user.getEmail(), e);
        }
    }

    /**
     * Send password reset email
     * 
     * @param user user requesting reset
     * @param token reset token
     */
    @Async
    public void sendPasswordResetEmail(User user, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Password Reset Request - E-Commerce Platform");
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "We received a request to reset your password.\n\n" +
                    "Please reset your password by clicking the link below:\n" +
                    "http://localhost:8080/reset-password?token=%s\n\n" +
                    "If you did not request a password reset, please ignore this email.\n" +
                    "This link will expire in 24 hours.\n\n" +
                    "Best regards,\n" +
                    "E-Commerce Team",
                    user.getFullName(), token
            ));

            mailSender.send(message);
            logger.info("Password reset email sent to: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", user.getEmail(), e);
        }
    }

    /**
     * Send order confirmation email
     * 
     * @param order confirmed order
     */
    @Async
    public void sendOrderConfirmationEmail(Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(order.getUser().getEmail());
            message.setSubject("Order Confirmation - " + order.getOrderNumber());
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "Thank you for your order!\n\n" +
                    "Order Number: %s\n" +
                    "Order Date: %s\n" +
                    "Total Amount: $%.2f\n\n" +
                    "We will send you another email when your order ships.\n\n" +
                    "You can track your order at:\n" +
                    "http://localhost:8080/orders/%s\n\n" +
                    "Best regards,\n" +
                    "E-Commerce Team",
                    order.getUser().getFullName(),
                    order.getOrderNumber(),
                    order.getOrderDate(),
                    order.getTotalAmount(),
                    order.getOrderNumber()
            ));

            mailSender.send(message);
            logger.info("Order confirmation email sent for order: {}", order.getOrderNumber());
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email for order: {}", order.getOrderNumber(), e);
        }
    }

    /**
     * Send order shipped notification
     * 
     * @param order shipped order
     */
    @Async
    public void sendOrderShippedEmail(Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(order.getUser().getEmail());
            message.setSubject("Your Order Has Shipped - " + order.getOrderNumber());
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "Great news! Your order has shipped.\n\n" +
                    "Order Number: %s\n" +
                    "Tracking Number: %s\n" +
                    "Carrier: %s\n\n" +
                    "You can track your shipment using the tracking number above.\n\n" +
                    "Expected Delivery: %s\n\n" +
                    "Best regards,\n" +
                    "E-Commerce Team",
                    order.getUser().getFullName(),
                    order.getOrderNumber(),
                    order.getTrackingNumber(),
                    order.getCarrier(),
                    order.getExpectedDeliveryDate()
            ));

            mailSender.send(message);
            logger.info("Order shipped email sent for order: {}", order.getOrderNumber());
        } catch (Exception e) {
            logger.error("Failed to send order shipped email for order: {}", order.getOrderNumber(), e);
        }
    }

    /**
     * Send welcome email to new user
     * 
     * @param user new user
     */
    @Async
    public void sendWelcomeEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Welcome to E-Commerce Platform!");
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "Welcome to E-Commerce Platform!\n\n" +
                    "We're excited to have you as part of our community.\n\n" +
                    "Start shopping now and enjoy exclusive deals and offers.\n\n" +
                    "Visit us at: http://localhost:8080\n\n" +
                    "If you have any questions, feel free to contact our support team.\n\n" +
                    "Happy shopping!\n\n" +
                    "Best regards,\n" +
                    "E-Commerce Team",
                    user.getFullName()
            ));

            mailSender.send(message);
            logger.info("Welcome email sent to: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}", user.getEmail(), e);
        }
    }
}
