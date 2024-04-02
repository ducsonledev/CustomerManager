package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRegistrationRequest(
                @NotBlank
                String name,
                @NotBlank
                String email,
                @NotBlank
                String password,
                @NotNull
                Integer age,
                @NotNull
                Gender gender,
                @NotNull
                Role role // TODO default USER // issue @Nullable not working
) {}
