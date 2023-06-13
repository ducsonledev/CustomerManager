package com.demospringfullstack.springbootexample.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {

    List<Customer> selectAllCustomer();
    Optional<Customer> selectCustomerById(Integer Id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);

    void removeCustomerById(Integer id);

    boolean existsPersonWithId(Integer id);

    void updateCustomer(Customer updatedCustomer);
}
