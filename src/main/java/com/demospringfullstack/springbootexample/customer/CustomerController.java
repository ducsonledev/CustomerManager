package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers") // route path
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class CustomerController {
    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_READ') or hasAuthority('MANAGER_READ')")
    public List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public CustomerDTO getCustomer(
            @PathVariable("id") Long id
    ) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
        String token = jwtUtil.issueToken(request.email(), request.role().name());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(
            @PathVariable Long id) {
        customerService.removeCustomerById(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateRequest updateRequest
    ) {
        customerService.updateCustomer(id, updateRequest);
    }
}
