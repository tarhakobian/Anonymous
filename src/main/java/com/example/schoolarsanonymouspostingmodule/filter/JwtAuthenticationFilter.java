package com.example.schoolarsanonymouspostingmodule.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.schoolarsanonymouspostingmodule.config.WebSecurityConfig;
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

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays
                .asList("/login", "/register")
                .contains(request.getRequestURI());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            var jwtToken = authHeader.replace("Bearer ", "");
            SecurityContextHolder.getContext().setAuthentication(parse(jwtToken));
        }

        filterChain.doFilter(request, response);
    }

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
