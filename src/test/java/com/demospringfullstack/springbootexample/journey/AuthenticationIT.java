package com.demospringfullstack.springbootexample.journey;

import com.demospringfullstack.springbootexample.customer.Customer;
import com.demospringfullstack.springbootexample.customer.CustomerRegistrationRequest;
import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
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
public class AuthenticationIT {
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String AUTHENTICATION_PATH = "/api/v1/authentication";
    private static final String CUSTOMER_PATH = "/api/v1/customer";

    @Test
    void canLoginCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        Random random = new Random();
        var gender = (random.nextInt(0, 2) == 0) ? Gender.MALE : Gender.FEMALE;
        String name = fakerName.fullName();
        String email = fakerName.lastName()
                + UUID.randomUUID() + "@mailservice2323.com";
        int age = RANDOM.nextInt(1, 100);
        Role role = Role.USER;
        var request = new CustomerRegistrationRequest(
                name,
                email,
                "password",
                age,
                gender,
                role
        );

        // send a post request
        webTestClient.post()
                .uri(AUTHENTICATION_PATH)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(AUTHENTICATION_PATH)
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
                name,
                email,
                "password",
                age,
                gender,
                role
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
                .uri(AUTHENTICATION_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

}
