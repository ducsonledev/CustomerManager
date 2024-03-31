package com.demospringfullstack.springbootexample.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerUpdateRequest (
        @NotBlank
        String name,
        @NotBlank
        @Email(message = "Requires valid email address")
        String email,
        @NotNull
        Integer age
) {
}
