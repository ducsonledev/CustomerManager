package com.demospringfullstack.springbootexample.auth;

public record AuthenticationRequest(
        String email,
        String password
) {
}
