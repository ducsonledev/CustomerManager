package com.demospringfullstack.springbootexample.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {

    List<Customer> selectAllCustomer();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);

    void removeCustomerById(Long id);

    boolean existsPersonWithId(Long id);

    void updateCustomer(Customer updatedCustomer);

    Optional<Customer> selectUserByEmail(String email);
}
