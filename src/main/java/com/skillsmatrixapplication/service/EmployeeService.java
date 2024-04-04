package com.skillsmatrixapplication.service;


import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
            existingEmployee.setOwners(newEmployeeDetails.getOwners());
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}