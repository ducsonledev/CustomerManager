package com.demospringfullstack.springbootexample.auth;

import com.demospringfullstack.springbootexample.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/login")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationProvider authenticationProvider;
    private final JWTUtil jwtUtil;
    /*
    @PostMapping
    public ResponseEntity<?> loginCustomer(
            @RequestBody AuthenticationRequest request) {
        //customerService.addCustomer(request);
        authenticationProvider.authenticate(request.email(), request.password());
        String token = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }*/
}
