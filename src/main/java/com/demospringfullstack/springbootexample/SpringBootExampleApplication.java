package com.demospringfullstack.springbootexample;

import com.demospringfullstack.springbootexample.customer.Customer;
import com.demospringfullstack.springbootexample.customer.CustomerRepository;
import com.demospringfullstack.springbootexample.enums.Gender;
import com.demospringfullstack.springbootexample.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class SpringBootExampleApplication {
	public static void main(String[] args) {

		SpringApplication.run(SpringBootExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(
			CustomerRepository customerRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			var faker = new Faker();
			var random = new Random();
			var name = faker.name();
			String firstName = name.firstName();
			String lastName = name.lastName();
			var gender = (random.nextInt(0, 2) == 0) ? Gender.MALE : Gender.FEMALE;
			Role[] roles = Role.values();
			var role = Arrays.asList(roles).get(random.nextInt(roles.length - 1));
			var customer = new Customer(
					firstName + " " + lastName,
					firstName.toLowerCase() + "." + lastName.toLowerCase()
							+ "@mailservice.com",
					passwordEncoder.encode(UUID.randomUUID().toString()), random.nextInt(16, 99),
					gender,
					role
			);
			customerRepository.save(customer);
		};
	}
}
