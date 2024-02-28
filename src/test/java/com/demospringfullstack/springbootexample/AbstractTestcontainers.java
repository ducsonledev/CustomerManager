package com.demospringfullstack.springbootexample;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestcontainers {

    protected static final Faker FAKER = new Faker();

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway
                .configure()
                .dataSource(
                        postgreSQLContainer.getJdbcUrl(),
                        postgreSQLContainer.getUsername(),
                        postgreSQLContainer.getPassword()
                ).load();
        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("sonscode-dao-unit-test")
                .withUsername("sonscode")
                .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        // map application.yml to our test container
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
