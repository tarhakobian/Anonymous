package com.anonymous.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.anonymous.config.WebSecurityConfig;
import com.anonymous.exception.AccountIsLockedException;
import com.anonymous.model.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

/**
 * Filter for processing login requests. Reads the login request from the request body,
 * authenticates the user using the AuthenticationManager, and generates a JWT token upon successful authentication.
 * <p>
 * Author
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Attempts to authenticate the user using the credentials provided in the login request.
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @return the authentication object if authentication is successful
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles successful authentication by generating a JWT token and adding it to the response header.
     *
     * @param request        the HTTP servlet request
     * @param response       the HTTP servlet response
     * @param chain          the filter chain
     * @param authentication the authentication object
     * @throws IOException      if an I/O error occurs during the response handling
     * @throws ServletException if a servlet exception occurs during the response handling
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        var principal = ((User) authentication.getPrincipal());

        if (!principal.isAccountNonLocked()) {
            throw new AccountIsLockedException();
        }

        var builder = JWT.create()
                .withSubject(principal.getUsername());

        var jwtToken = builder
                .withArrayClaim("authorities", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + WebSecurityConfig.JWT_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(WebSecurityConfig.JWT_SECRET.getBytes()));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
