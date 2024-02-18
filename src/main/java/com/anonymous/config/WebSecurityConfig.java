package com.anonymous.config;

import com.anonymous.filter.JwtAuthenticationFilter;
import com.anonymous.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration class for setting up Spring Security.
 * <p>
 * Author : Taron Hakobyan
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * The secret key for JWT authentication.
     */
    public static final String JWT_SECRET = "SECRET";

    /**
     * The expiration time for JWT tokens (in milliseconds).
     * Here, it is set to 1 day.
     */
    public static final int JWT_EXPIRATION_TIME = 86400000; // 1 day

    /**
     * Configures the security filter chain for the application.
     *
     * @param httpSecurity          The HttpSecurity object for configuring security settings.
     * @param authenticationManager The AuthenticationManager for handling authentication.
     * @return A SecurityFilterChain configured for the application.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        return httpSecurity
                // Configure Cross-Origin Resource Sharing (CORS)
                .cors(Customizer.withDefaults())
                // Disable Cross-Site Request Forgery (CSRF) protection
                .csrf(AbstractHttpConfigurer::disable)
                // Add custom LoginFilter for handling login requests
                .addFilter(new LoginFilter(authenticationManager))
                // Add JwtAuthenticationFilter to validate JWT tokens in requests
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Creates and configures the AuthenticationManager bean.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration for creating the AuthenticationManager.
     * @return An AuthenticationManager bean.
     * @throws Exception If an error occurs during authentication manager creation.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) settings.
     *
     * @return A CorsConfigurationSource with allowed origins and methods.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from any origin
        configuration.setAllowedOrigins(List.of("*"));
        // Allow all HTTP methods
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Register the CORS configuration for all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Creates a BCryptPasswordEncoder bean for password encoding.
     *
     * @return A BCryptPasswordEncoder bean with strength 11.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
