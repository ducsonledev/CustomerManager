package com.demospringfullstack.springbootexample.journey;

import com.demospringfullstack.springbootexample.customer.Customer;
import com.demospringfullstack.springbootexample.customer.CustomerDTO;
import com.demospringfullstack.springbootexample.customer.CustomerRegistrationRequest;
import com.demospringfullstack.springbootexample.customer.CustomerUpdateRequest;
import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        Role role = Role.ADMIN;
        var request = new CustomerRegistrationRequest(
                name, 
                email, 
                "password", 
                age, 
                gender,
                role
        );

        // send a post request
        String registerURI = CUSTOMER_URI + "/register";
        String jwtToken = webTestClient.post()
                .uri(registerURI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange() // send the request
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // make sure that customer is present with id
        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                role,
                age,
                role.getAuthorities().stream().map(SimpleGrantedAuthority::toString).toList(),
                email // username = email
        );

        assertThat(allCustomers).contains(expectedCustomer);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
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
        Role role = Role.ADMIN;
        var request = new CustomerRegistrationRequest(
                name,
                email,
                "password",
                age,
                gender,
                role
        );

        // send a post request
        String registerURI = CUSTOMER_URI + "/register";
        String jwtToken = webTestClient.post()
                .uri(registerURI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        // register a new customer for checking if customer before still exists or not
        String newEmail = fakerName.lastName()
                + UUID.randomUUID() + "@mailservice2323.com";
        request = new CustomerRegistrationRequest(
                name,
                newEmail,
                "password",
                age,
                gender,
                role
        );

        String newJwtToken = webTestClient.post()
                .uri(registerURI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", newJwtToken))
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
        Role role = Role.ADMIN;
        var request = new CustomerRegistrationRequest(
                name,
                email,
                "password",
                age,
                gender,
                role
        );

        // send a post request
        String registerURI = CUSTOMER_URI + "/register";
        String jwtToken = webTestClient.post()
                .uri(registerURI)// no localhost/ports in here, just to locate where in our api
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class) // attach request in test
                .exchange()// send the request
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
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
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()// send the request
                .expectStatus()
                .isOk();

        // get customer by id
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                updatedName,
                email,
                gender,
                role,
                age,
                role.getAuthorities().stream().map(SimpleGrantedAuthority::toString).toList(),
                email
        );

        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }
}
