package com.demospringfullstack.springbootexample.customer;

import com.demospringfullstack.springbootexample.enums.Role;
import com.demospringfullstack.springbootexample.security.jwt.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers") // route path
//@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class CustomerController {
    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    @PostMapping("register")
    public ResponseEntity<?> registerCustomer(
            @RequestBody @Valid CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
        String token = jwtUtil.issueToken(
                request.email(),
                request.role() != null ? request.role().name() : Role.USER.name()
        );
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }

    @GetMapping
    @PreAuthorize("(hasRole('ADMIN') or hasRole('MANAGER')) " +
            "and (hasAuthority('ADMIN_READ') or hasAuthority('MANAGER_READ'))")
    public List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    @PreAuthorize("(hasRole('ADMIN') or hasRole('MANAGER')) " +
            "and (hasAuthority('ADMIN_READ') or hasAuthority('MANAGER_READ'))")
    public CustomerDTO getCustomer(
            @PathVariable("id") Long id
    ) {
        return customerService.getCustomer(id);
    }

    @GetMapping("/currentCustomer")
    @PreAuthorize("(hasRole('ADMIN') or hasRole('MANAGER')) " +
            "and (hasAuthority('ADMIN_READ') or hasAuthority('MANAGER_READ'))")
    public CustomerDTO getCurrentCustomer() {
        return customerService.getCurrentCustomer();
    }

    @PutMapping("{id}")
    @PreAuthorize("(hasRole('ADMIN') or hasRole('MANAGER')) " +
            "and (hasAuthority('ADMIN_UPDATE') or hasAuthority('MANAGER_UPDATE'))")
    public void updateCustomer(
            @PathVariable Long id,
            @RequestBody @Valid CustomerUpdateRequest updateRequest
    ) {
        customerService.updateCustomer(id, updateRequest);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("(hasRole('ADMIN') or hasRole('MANAGER')) " +
            "and (hasAuthority('ADMIN_DELETE') or hasAuthority('MANAGER_DELETE'))")
    public void deleteCustomer(
            @PathVariable Long id) {
        customerService.removeCustomerById(id);
    }
}
