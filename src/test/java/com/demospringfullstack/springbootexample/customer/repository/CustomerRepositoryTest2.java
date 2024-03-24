package com.demospringfullstack.springbootexample.customer.repository;

import com.demospringfullstack.springbootexample.AbstractTestcontainers;
import com.demospringfullstack.springbootexample.customer.Customer;
import com.demospringfullstack.springbootexample.customer.CustomerRepository;
import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest2 extends AbstractTestcontainers {
    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        var customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.MALE,
                Role.USER
        );
        underTest.save(customer);

        // When
        var actual = underTest.existsByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        var customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.FEMALE,
                Role.USER
        );
        underTest.save(customer);

        Long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Long id = -1L;

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
    }
}