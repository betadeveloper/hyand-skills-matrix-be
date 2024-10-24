package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.LoginEmployeeRequest;
import com.skillsmatrixapplication.dto.RegisterEmployeeRequest;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import com.skillsmatrixapplication.persistence.entity.Role;
import com.skillsmatrixapplication.enums.RoleEnum;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRoleRepository;
import com.skillsmatrixapplication.persistence.repository.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRoleRepository employeeRoleRepository;

    public AuthenticationService(
            EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, EmployeeRoleRepository employeeRoleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRoleRepository = employeeRoleRepository;
    }

    public Employee signup(RegisterEmployeeRequest input) {
        Employee employee = new Employee();
        employee.setFirstName(input.getFirstName());
        employee.setLastName(input.getLastName());
        employee.setEmail(input.getEmail());
        employee.setPassword(passwordEncoder.encode(input.getPassword()));

        if(roleRepository.findByRole(RoleEnum.ROLE_EMPLOYEE).isEmpty() && roleRepository.findByRole(RoleEnum.ROLE_OWNER).isEmpty() && roleRepository.findByRole(RoleEnum.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleEnum.ROLE_EMPLOYEE, "Employee"));
            roleRepository.save(new Role(RoleEnum.ROLE_OWNER, "Owner"));
            roleRepository.save(new Role(RoleEnum.ROLE_ADMIN, "Admin"));
        }

        employeeRoleRepository.save(new EmployeeRole(employee, roleRepository.findByRole(RoleEnum.ROLE_EMPLOYEE).get()));

        return employeeRepository.save(employee);
    }

    public Employee authenticate(LoginEmployeeRequest input) {
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
