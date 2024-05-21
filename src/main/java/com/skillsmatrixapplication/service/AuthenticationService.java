package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.LoginUserRequest;
import com.skillsmatrixapplication.dto.RegisterUserRequest;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            EmployeeRepository employeeRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee signup(RegisterUserRequest input) {
        Employee employee = new Employee();
        employee.setFirstName(input.getFirstName());
        employee.setLastName(input.getLastName());
        employee.setEmail(input.getEmail());
        employee.setPassword(passwordEncoder.encode(input.getPassword()));

        return employeeRepository.save(employee);
    }

    public Employee authenticate(LoginUserRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return employeeRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
