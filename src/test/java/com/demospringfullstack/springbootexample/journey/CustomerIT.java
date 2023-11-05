package com.demospringfullstack.springbootexample.journey;

import com.demospringfullstack.springbootexample.customer.Customer;
import com.demospringfullstack.springbootexample.customer.CustomerRegistrationRequest;
import com.demospringfullstack.springbootexample.customer.CustomerUpdateRequest;
import com.demospringfullstack.springbootexample.customer.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {

    @Autowired
    private WebTestClient webTestClient; // replaces postman

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        Random random = new Random();
        var gender = (random.nextInt(0, 2) == 0) ? Gender.MALE : Gender.FEMALE;
        String name = fakerName.fullName();
        String email = fakerName.lastName()
                + UUID.randomUUID() + "@mailservice2323.com";
        int age = RANDOM.nextInt(1, 100);
        var request = new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );

        // send a post request

        webTestClient.post()
                .uri(CUSTOMER_URI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present
        Customer expectedCustomer = new Customer(
                name, email, "password", age, gender
        );

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        var id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        Random random = new Random();
        var gender = (random.nextInt(0, 2) == 0) ? Gender.MALE : Gender.FEMALE;
        String name = fakerName.fullName();
        String email = fakerName.lastName()
                + UUID.randomUUID() + "@mailservice2323.com";
        int age = RANDOM.nextInt(1, 100);
        var request = new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        Random random = new Random();
        var gender = (random.nextInt(0, 2) == 0) ? Gender.MALE : Gender.FEMALE;
        String name = fakerName.fullName();
        String email = fakerName.lastName()
                + UUID.randomUUID() + "@mailservice2323.com";
        int age = RANDOM.nextInt(1, 100);
        var request = new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer
        String updatedName = name + UUID.randomUUID();
        var updatedCustomerRequest = new CustomerUpdateRequest(
                updatedName, null, null
        );
        // updatedCustomer.setId(id);

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON) // client sends
                .body(Mono.just(updatedCustomerRequest), CustomerUpdateRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(
                id, updatedName, email, "password", age, gender
        );

        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }
}
