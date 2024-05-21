package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import com.skillsmatrixapplication.persistence.entity.Role;
import com.skillsmatrixapplication.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class UserRoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = roleService.createRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @PostMapping("/employees/{employeeId}/roles/{roleId}")
    public ResponseEntity<EmployeeRole> assignRoleToEmployee(@PathVariable Long employeeId, @PathVariable Long roleId) {
        EmployeeRole savedRole = roleService.assignRoleToEmployee(employeeId, roleId);
        return ResponseEntity.ok(savedRole);
    }
}
