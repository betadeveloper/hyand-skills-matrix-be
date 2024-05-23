package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OwnerController {

    private final EmployeeService employeeService;

    public OwnerController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees/{employeeId}/owners")
    public ResponseEntity<List<Employee>> getEmployeeOwners(@PathVariable Long employeeId) {
        return employeeService.getEmployeeOwners(employeeId);
    }

    @PostMapping("/employees/{employeeId}/owners/{ownerId}")
    public ResponseEntity<Employee> addOwner(@PathVariable Long employeeId, @PathVariable Long ownerId) {
        return employeeService.addOwner(employeeId, ownerId);
    }

    @DeleteMapping("/employees/{employeeId}/owners/{ownerId}")
    public ResponseEntity<Employee> removeOwner(@PathVariable Long employeeId, @PathVariable Long ownerId) {
        return employeeService.removeOwner(employeeId, ownerId);
    }
}
