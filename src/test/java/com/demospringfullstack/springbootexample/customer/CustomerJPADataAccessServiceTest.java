package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // init mock
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomer() {
        // Given
        // When
        underTest.selectAllCustomer();

        // Then
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        Long id = 1L;

        // When
        underTest.selectCustomerById(id);

        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        var customer = new Customer(
                1L,
                "john",
                "john@mailservice.com",
                "password",
                33,
                Gender.MALE,
                Role.USER
        );

        // When
        underTest.insertCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = "john@mailservice.com";

        // When
        underTest.existsPersonWithEmail(email);

        // Then
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        // Given
        Long id = 1L;

        // When
        underTest.existsPersonWithId(id);

        // Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void removeCustomerById() {
        // Given
        Long id = 1L;

        // When
        underTest.removeCustomerById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                1L,
                "john bolt",
                "johnbolt@mailservice.com",
                "password",
                33,
                Gender.MALE,
                Role.USER
        );

        // When
        underTest.updateCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }
}