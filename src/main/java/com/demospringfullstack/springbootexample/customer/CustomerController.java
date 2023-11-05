package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers") // route path
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomer(
            @PathVariable("id") Integer id
    ) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
        String token = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(
            @PathVariable Integer id) {
        customerService.removeCustomerById(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(
            @PathVariable Integer id,
            @RequestBody CustomerUpdateRequest updateRequest
    ) {
        customerService.updateCustomer(id, updateRequest);
    }
}
