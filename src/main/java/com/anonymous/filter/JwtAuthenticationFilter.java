package com.anonymous.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.anonymous.config.WebSecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Filter for JWT authentication. Parses JWT tokens from the Authorization header and sets the
 * authentication in the security context.
 * <p>
 *  Author: Taron Hakobyan
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Checks if the request URI should not be filtered.
     *
     * @param request the HTTP servlet request
     * @return true if the request URI is in the exclusion list, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays
                .asList("/login", "/register", "/users/activate")
                .contains(request.getRequestURI());
    }

    /**
     * Parses the JWT token from the Authorization header and sets the authentication in the
     * security context.
     *
     * @param request     the HTTP servlet request
     * @param response    the HTTP servlet response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during the filter process
     * @throws IOException      if an I/O error occurs during the filter process
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            var jwtToken = authHeader.replace("Bearer ", "");
            SecurityContextHolder.getContext().setAuthentication(parse(jwtToken));
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Parses the JWT token and constructs the authentication object.
     *
     * @param jwtToken the JWT token
     * @return the authentication object
     */
    private Authentication parse(String jwtToken) {
        var decodedJwt = JWT.require(Algorithm.HMAC512(WebSecurityConfig.JWT_SECRET.getBytes()))
                .build()
                .verify(jwtToken);

        var authorities = Arrays.stream(decodedJwt.getClaims().get("authorities").asArray(String.class))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(decodedJwt.getSubject(), null, authorities);
    }
}
