package com.demospringfullstack.springbootexample.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
