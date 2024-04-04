package com.skillsmatrixapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @GetMapping("/employees")
    public ResponseEntity<String> getEmployees() {
        return ResponseEntity.ok("Employees!");
    }
}