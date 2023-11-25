package com.demospringfullstack.springbootexample.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {

    // fake db
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1L, "Alex",  "test@mail.com", "password", 21, Gender.MALE
        );
        customers.add(alex);
        Customer jamila = new Customer(
                2L, "Jamila", "test@mail.com", "password", 33, Gender.FEMALE
        );
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomer() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }
    @Override
    public boolean existsPersonWithId(Long id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void removeCustomerById(Long id) {
        selectCustomerById(id)
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        insertCustomer(customer);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getUsername().equals(email))
                .findFirst();
    }
}
