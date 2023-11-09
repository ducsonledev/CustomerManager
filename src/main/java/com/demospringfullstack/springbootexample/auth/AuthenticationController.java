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
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationProvider authenticationProvider;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response); // remove later, no user information should be posted, no payloads like this wej ust returned
        // only HTTP GET requests for user information
    }
}
