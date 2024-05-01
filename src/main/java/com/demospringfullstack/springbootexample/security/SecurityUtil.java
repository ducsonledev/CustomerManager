package com.demospringfullstack.springbootexample.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

    public static Optional<String> getCurrentCustomerLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return Optional.ofNullable(extractPrincipal(authentication));
    }

    private static String extractPrincipal(Authentication authentication) {
        if(authentication == null)
            return null;

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();
        if (authentication.getPrincipal() instanceof String)
            return (String) principal;

        return null;
    }
}
