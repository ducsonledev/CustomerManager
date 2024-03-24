package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
import jakarta.annotation.Nullable;

public record CustomerRegistrationRequest(
                String name,
                String email,
                String password,
                Integer age,
                Gender gender,
                @Nullable
                Role role
) {}
