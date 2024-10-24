package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import com.skillsmatrixapplication.persistence.entity.Role;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRoleRepository;
import com.skillsmatrixapplication.persistence.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Transactional
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public EmployeeRole assignRoleToEmployee(Long employeeId, Long roleId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        EmployeeRole employeeRole = new EmployeeRole(employee, role);
        return employeeRoleRepository.save(employeeRole);
    }

    @Transactional
    public EmployeeRole getEmployeeRole(Long employeeId) {
        return employeeRoleRepository.findByEmployeeId(employeeId);
    }
}
