package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.LoginEmployeeRequest;
import com.skillsmatrixapplication.dto.LoginEmployeeResponse;
import com.skillsmatrixapplication.dto.RegisterEmployeeRequest;
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
    public ResponseEntity<Employee> register(@RequestBody RegisterEmployeeRequest registerEmployeeRequest) {
        Employee registeredUser = authenticationService.signup(registerEmployeeRequest);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginEmployeeResponse> authenticate(@RequestBody LoginEmployeeRequest loginEmployeeRequest) {
        Employee authenticatedEmployee = authenticationService.authenticate(loginEmployeeRequest);

        String jwtToken = jwtService.generateToken(authenticatedEmployee);

        LoginEmployeeResponse loginResponse = new LoginEmployeeResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}

