package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.exception.DuplicateResourceException;
import com.demospringfullstack.springbootexample.exception.RequestValidationException;
import com.demospringfullstack.springbootexample.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO, passwordEncoder, customerDTOMapper);
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        verify(customerDAO).selectAllCustomer();
    }

    @Test
    void canGetCustomer() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(
                id, "John", "john@mailservice.com", "password", 22, Gender.MALE
        );
        // if this works returns optional
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerDTO expected = customerDTOMapper.apply(customer);

        // When
        CustomerDTO actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(
                id, "John", "john@mailservice.com", "password", 22, Gender.MALE
        );
        // if this works returns optional
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        String email = "john@mailservice.com";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        var request = new CustomerRegistrationRequest(
           "John", email, "password", 21, Gender.MALE
        );

        String passwordHash = "hj4hjh4545;;kfg";

        // When
        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName().equals(request.name()));
        assertThat(capturedCustomer.getEmail().equals(request.email()));
        assertThat(capturedCustomer.getAge().equals(request.age()));
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        // Given
        String email = "john@mailservice.com";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        var request = new CustomerRegistrationRequest(
                "John", email, "password", 21, Gender.MALE
        );

        // When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exists");

        // Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void removeCustomerById() {
        // Given
        Long id = 10L;
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);

        // When
        underTest.removeCustomerById(id);

        // Then
        verify(customerDAO).removeCustomerById(id);
    }

    @Test
    void willThrowWhenIdNotExistsWhileRemoveCustomerById() {
        // Given
        Long id = 10L;
        when(customerDAO.existsPersonWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.removeCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        // Then
        verify(customerDAO, never()).removeCustomerById(any());
    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        Long id = 10L;
        String newEmail = "johnbolt@mailingservice.com";
        var request = new CustomerUpdateRequest(
                "John Bolt", newEmail, 33
        );
        Customer customer = new Customer(
                id, "John",  "john@mailservice.com", "password", 22, Gender.MALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        Long id = 10L;
        var request = new CustomerUpdateRequest(
                "John Bolt", null, null
        );
        Customer customer = new Customer(
                id, "John",  "john@mailservice.com", "password", 22, Gender.MALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        Long id = 10L;
        String newEmail = "johnbolt@mailingservice.com";
        var request = new CustomerUpdateRequest(
                null, newEmail, null
        );
        Customer customer = new Customer(
                id, "John", "john@mailservice.com", "password", 22, Gender.MALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        Long id = 10L;
        var request = new CustomerUpdateRequest(
                null, null, 33
        );
        Customer customer = new Customer(
                id, "John", "john@mailservice.com", "password", 22,  Gender.MALE
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenUpdateCustomerReturnEmptyOptional() {
        // Given
        Long id = 10L;
        boolean changes = false;
        var request = new CustomerUpdateRequest(
                "John Bolt", "john@mailingservice.com", 33
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenEmailExistsWhileUpdateCustomer() {
        // Given
        Long id = 10L;
        // boolean changes = false;
        String newEmail = "johnny@mailservice.com";
        var request = new CustomerUpdateRequest(
                "John Bolt", newEmail, 33
        );
        Customer customer = new Customer(
                id, "John",  "john@mailservice.com", "password",22, Gender.MALE
        );
        Customer johnny = new Customer(
                1L, "Johnny", "johnny@mailservice.com", "password", 21, Gender.MALE
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenNoChangesWhileUpdateCustomer() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(
                id, "John", "john@mailservice.com", "password",22, Gender.MALE
                );
        var request = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        // Then
        verify(customerDAO, never()).updateCustomer(any());
    }
}