package com.demospringfullstack.springbootexample.customer;

import jakarta.validation.constraints.Email;

public record CustomerUpdateRequest (
        String name,
        @Email(message = "Requires valid email address")
        String email,
        Integer age
) {
}
