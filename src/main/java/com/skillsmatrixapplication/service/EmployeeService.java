package com.skillsmatrixapplication.service;


import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Owner;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final OwnerRepository ownerRepository;

    public EmployeeService(EmployeeRepository employeeRepository, OwnerRepository ownerRepository) {
        this.employeeRepository = employeeRepository;
        this.ownerRepository = ownerRepository;
    }

    public ResponseEntity<Employee> updateEmployee(Long id, Employee newEmployeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setFirstName(newEmployeeDetails.getFirstName());
            existingEmployee.setLastName(newEmployeeDetails.getLastName());
            existingEmployee.setEmail(newEmployeeDetails.getEmail());
            existingEmployee.setPosition(newEmployeeDetails.getPosition());
            existingEmployee.setDepartment(newEmployeeDetails.getDepartment());
            existingEmployee.setProfilePicture(newEmployeeDetails.getProfilePicture());
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Employee> updateCurrentEmployee(Employee newEmployeeDetails) {
        Employee currentEmployee = employeeRepository.findByEmail(newEmployeeDetails.getEmail()).orElseThrow();
        currentEmployee.setFirstName(newEmployeeDetails.getFirstName());
        currentEmployee.setLastName(newEmployeeDetails.getLastName());
        currentEmployee.setEmail(newEmployeeDetails.getEmail());
        currentEmployee.setPosition(newEmployeeDetails.getPosition());
        currentEmployee.setDepartment(newEmployeeDetails.getDepartment());
        currentEmployee.setProfilePicture(newEmployeeDetails.getProfilePicture());
        Employee updatedEmployee = employeeRepository.save(currentEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Transactional
    public ResponseEntity<Employee> addOwner(Long employeeId, Long ownerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee ownerEmployee = employeeRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Owner owner = new Owner(employee, ownerEmployee);
        ownerRepository.save(owner);

        return ResponseEntity.ok(employee);
    }

    @Transactional
    public ResponseEntity<Employee> removeOwner(Long employeeId, Long ownerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee ownerEmployee = employeeRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Owner owner = ownerRepository.findByEmployeeAndOwner(employee, ownerEmployee)
                .orElseThrow(() -> new RuntimeException("Owner relationship not found"));
        ownerRepository.delete(owner);

        return ResponseEntity.ok(employee);
    }

    @Transactional
    public ResponseEntity<List<Set<Employee>>> getEmployeeOwners(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<Set<Employee>> owners = employee.getOwners().stream()
                    .map(Employee::getOwners)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(owners);
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @Transactional
    public ResponseEntity<List<Employee>> getCurrentEmployeeOwners() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmployeeEmail = authentication.getName();

        Employee currentEmployee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"));

        List<Employee> owners = new ArrayList<>(currentEmployee.getOwners());

        return ResponseEntity.ok(owners);
    }
}