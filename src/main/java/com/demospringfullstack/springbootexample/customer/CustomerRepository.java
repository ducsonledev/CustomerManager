package com.demospringfullstack.springbootexample.customer;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Transactional
public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {

    // JPA will construct the sql query for us without @Query
    // 4.4 Defining Query Methods 4.4.2 Query Creation
    // By deriving the query from the method name directly.
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    Optional<Customer> findCustomerByEmail(String email);

}
