package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.EmployeeResponse;
import com.skillsmatrixapplication.enums.CareerLevel;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.service.EmployeeService;
import com.skillsmatrixapplication.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final EmployeeService employeeService;

    private final RoleService roleService;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService, RoleService roleService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    @GetMapping("/employee/current")
    public ResponseEntity<Employee> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Employee currentEmployee = (Employee) authentication.getPrincipal();

        EmployeeRole currentRole = roleService.getEmployeeRole(currentEmployee.getId());

        currentEmployee.setCurrentRole(currentRole.getRole().getRole().name());

        return ResponseEntity.ok(currentEmployee);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return ResponseEntity.ok(employees);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeResponse newEmployeeDetails) {
        return employeeService.updateEmployee(id, newEmployeeDetails);
    }

    @PutMapping("/employee/current")
    public ResponseEntity<EmployeeResponse> updateCurrentEmployee(@RequestBody EmployeeResponse newEmployeeDetails) {
        return employeeService.updateCurrentEmployee(newEmployeeDetails);
    }

    @PutMapping("/employee/current/careerLevel")
    public ResponseEntity<EmployeeResponse> updateCurrentEmployeeCareerLevel(@RequestBody String newCareerLevel) {
        return employeeService.updateCareerLevel(CareerLevel.valueOf(newCareerLevel));
    }

    @PostMapping("/{employeeId}/careerPaths/{careerPathId}/skills")
    public ResponseEntity<Void> assignSkillsToEmployee(@PathVariable Long employeeId,
                                                       @PathVariable Long careerPathId,
                                                       @RequestBody List<Long> skillIds) {
        employeeService.assignSkillsToEmployee(employeeId, careerPathId, skillIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{employeeId}/careerPaths/{careerPathId}")
    public ResponseEntity<Void> assignCareerPathToEmployee(@PathVariable Long employeeId,
                                                           @PathVariable Long careerPathId) {
        employeeService.assignCareerPathToEmployee(employeeId, careerPathId);
        return ResponseEntity.ok().build();
    }
}