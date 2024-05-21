package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.LoginUserRequest;
import com.skillsmatrixapplication.dto.LoginUserResponse;
import com.skillsmatrixapplication.dto.RegisterUserRequest;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.security.JwtService;
import com.skillsmatrixapplication.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Employee> register(@RequestBody RegisterUserRequest registerUserRequest) {
        Employee registeredUser = authenticationService.signup(registerUserRequest);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> authenticate(@RequestBody LoginUserRequest loginUserRequest) {
        Employee authenticatedEmployee = authenticationService.authenticate(loginUserRequest);

        String jwtToken = jwtService.generateToken(authenticatedEmployee);

        LoginUserResponse loginResponse = new LoginUserResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}

