package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.enums.Role;
import com.demospringfullstack.springbootexample.exception.custom.DuplicateResourceException;
import com.demospringfullstack.springbootexample.exception.custom.RequestValidationException;
import com.demospringfullstack.springbootexample.exception.custom.ResourceNotFoundException;
import com.demospringfullstack.springbootexample.exception.message.ErrorMessage;
import com.demospringfullstack.springbootexample.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;

    @Autowired
    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO,
                           PasswordEncoder passwordEncoder,
                           CustomerDTOMapper customerDTOMapper) {
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.selectAllCustomer()
                .stream()
                .map(customerDTOMapper).collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long id) {
        return customerDAO.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessage.CUSTOMER_NOT_FOUND_EXCEPTION.formatted(id)
                ));
    }

    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDAO.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException(
                    ErrorMessage.EMAIL_ALREADY_EXISTS_EXCEPTION
            );
        }
        customerDAO.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        passwordEncoder.encode(customerRegistrationRequest.password()),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender(),
                        customerRegistrationRequest.role() != null ? customerRegistrationRequest.role() : Role.USER
                )
        );
    }

    public void removeCustomerById(Long id) {
        if (!customerDAO.existsPersonWithId(id))
            throw new ResourceNotFoundException(
                    ErrorMessage.CUSTOMER_NOT_FOUND_EXCEPTION.formatted(id)
            );
        customerDAO.removeCustomerById(id);
    }

    public void updateCustomer(
            Long id,
            CustomerUpdateRequest updateRequest
    ) {
        Customer customer = customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessage.CUSTOMER_NOT_FOUND_EXCEPTION.formatted(id)
                ));

        boolean changes = false;
        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDAO.existsPersonWithEmail(updateRequest.email()))
                throw new DuplicateResourceException(ErrorMessage.EMAIL_ALREADY_EXISTS_EXCEPTION);
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) throw new RequestValidationException(ErrorMessage.NO_DATA_CHANGES_FOUND_EXCEPTION);

        customerDAO.updateCustomer(customer);
    }

    public CustomerDTO getCustomerByEmail(String email) {
        return customerDAO.selectCustomerByEmail(email)
                .map(customerDTOMapper)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                            ErrorMessage.CUSTOMER_NOT_FOUND_EXCEPTION));
    }

    public CustomerDTO getCurrentCustomer() {
        String email = SecurityUtil.getCurrentCustomerLogin()
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                ErrorMessage.PRINCIPAL_NOT_FOUND_EXCEPTION));
        return getCustomerByEmail(email);
    }
}
