package com.demospringfullstack.springbootexample.customer;

public record CustomerUpdateRequest (
        String name,
        String email,
        Integer age
) {
}
