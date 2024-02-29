package com.demospringfullstack.springbootexample.security.jwt;

import com.demospringfullstack.springbootexample.customer.CustomerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final CustomerUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = getAuthorization(request);
        // anyone with the bearer token can impersonate you
        if (!isJwtAuth(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        // if rejected
        getAuth(request, getJwtToken(authHeader));
        filterChain.doFilter(request, response); // moving to the next filter on the chain
    }

    private void getAuth(HttpServletRequest request, String jwt) {
        String subject = jwtUtil.getSubject(jwt);
        if (isNotAuthenticated(subject)) {
            authenticateSubject(request, jwt, subject);
        }
    }

    private void authenticateSubject(HttpServletRequest request, String jwt, String subject) {
        var userDetails = customerUserDetailsService.loadUserByUsername(subject);
        if(jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
            var authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                   null,
                            userDetails.getAuthorities()
                    );
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private boolean isNotAuthenticated(String subject) {
        return subject != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private String getJwtToken(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isJwtAuth(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

}
