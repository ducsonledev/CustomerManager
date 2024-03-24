package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;

import java.util.List;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        Gender gender,
        Role role,
        int age,
        List<String> roles,
        String username
) {

}
