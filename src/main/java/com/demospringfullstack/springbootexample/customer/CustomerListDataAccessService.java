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
                1, "Alex",  "test@mail.com", "password", 21, Gender.MALE
        );
        customers.add(alex);
        Customer jamila = new Customer(
                2, "Jamila", "test@mail.com", "password", 33, Gender.FEMALE
        );
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomer() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
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
    public boolean existsPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void removeCustomerById(Integer id) {
        selectCustomerById(id)
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        insertCustomer(customer);
    }
}
