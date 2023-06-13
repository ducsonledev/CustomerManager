package com.demospringfullstack.springbootexample;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.github.javafaker.Faker;

import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers
public abstract class AbstractTestcontainers {

    protected static final Faker FAKER = new Faker();

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("sonscode-dao-unit-test")
            .withUsername("sonscode")
            .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        // map application.yml to our testcontainer
        registry.add(
        "spring.datasource.url",
        postgreSQLContainer::getJdbcUrl
        );
        registry.add(
        "spring.datasource.username",
        postgreSQLContainer::getUsername
        );
        registry.add(
        "spring.datasource.password",
        postgreSQLContainer::getPassword
        );
    }


}
