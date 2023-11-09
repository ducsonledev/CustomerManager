package com.demospringfullstack.springbootexample.auth;

import com.demospringfullstack.springbootexample.customer.CustomerDTO;

public record AuthenticationResponse(
    String token,
    CustomerDTO customerDTO
) {
}
