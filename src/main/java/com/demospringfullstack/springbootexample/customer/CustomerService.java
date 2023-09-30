package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.exception.DuplicateResourceException;
import com.demospringfullstack.springbootexample.exception.RequestValidationException;
import com.demospringfullstack.springbootexample.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomer();
    }
    public Customer getCustomer(Integer id) {
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDAO.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException(
                    "email already exists"
            );
        }
        customerDAO.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender()
                )
        );
    }

    public void removeCustomerById(Integer id) {
        if(!customerDAO.existsPersonWithId(id))
            throw new ResourceNotFoundException(
                    "customer with id [%s] not found".formatted(id)
            );
        customerDAO.removeCustomerById(id);
    }

    public void updateCustomer(
            Integer id,
            CustomerUpdateRequest updateRequest
    ) {
        var customer = getCustomer(id);

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
                throw new DuplicateResourceException("email already taken");
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) throw new RequestValidationException("No data changes found");

        customerDAO.updateCustomer(customer);

    }
}
