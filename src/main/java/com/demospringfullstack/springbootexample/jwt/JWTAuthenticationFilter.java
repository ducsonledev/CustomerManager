package com.demospringfullstack.springbootexample.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // anyone with the bearer token can impersonate you
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        }
        // if rejected
        String jwt = authHeader.substring(7); // "Bearer " onwards is our jwt token
        String subject = jwtUtil.getSubject(jwt);
    }
}
