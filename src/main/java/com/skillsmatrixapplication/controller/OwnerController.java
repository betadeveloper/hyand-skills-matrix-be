package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.EmployeeResponse;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final EmployeeService employeeService;

    public OwnerController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Set<Employee>>> getEmployeeOwners(@PathVariable Long employeeId) {
        return employeeService.getEmployeeOwners(employeeId);
    }

    @GetMapping("/currentEmployee")
    public ResponseEntity<List<Employee>> getCurrentEmployeeOwners() {
        return employeeService.getCurrentEmployeeOwners();
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> addOwner(@PathVariable Long employeeId, @RequestBody Employee owner) {
        return employeeService.addOwner(employeeId, owner);
    }

    @PostMapping("/currentEmployee")
    public ResponseEntity<EmployeeResponse> addCurrentEmployeeOwner(@RequestBody Employee owner) {
        return employeeService.addCurrentEmployeeOwner(owner);
    }
}
