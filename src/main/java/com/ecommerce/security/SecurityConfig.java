package com.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for the application
 * Configures authentication, authorization, and security filters
 * 
 * @author E-Commerce Team
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/h2-console/**"))
                        .disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Static resources (HTML, CSS, JS, images)
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/index.html")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/login.html")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/cart.html")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/images/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/favicon.ico")).permitAll()
                        
                        // Public endpoints - use AntPathRequestMatcher explicitly
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/auth/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/public/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api-docs/**")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-ui.html")).permitAll()
                        
                        // Product endpoints
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/products/**", "GET")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/categories/**", "GET")).permitAll()
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/reviews/**", "GET")).permitAll()
                        
                        // Admin endpoints
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/admin/**")).hasRole("ADMIN")
                        
                        // Seller endpoints
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/seller/**")).hasAnyRole("SELLER", "ADMIN")
                        
                        // All other API endpoints require authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // For H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
