package com.skillsmatrixapplication.service;


import com.skillsmatrixapplication.dto.AddOwnerDTO;
import com.skillsmatrixapplication.dto.EmployeeResponse;
import com.skillsmatrixapplication.exception.ExceptionMessages;
import com.skillsmatrixapplication.exception.ResourceNotFoundException;
import com.skillsmatrixapplication.enums.CareerLevel;
import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Skill;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.SkillRepository;
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


    private final CareerPathRepository careerPathRepository;

    private final SkillRepository skillRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CareerPathRepository careerPathRepository, SkillRepository skillRepository) {
        this.employeeRepository = employeeRepository;
        this.careerPathRepository = careerPathRepository;
        this.skillRepository = skillRepository;
    }

    @Transactional
    public void assignCareerPathToEmployee(Long employeeId, Long careerPathId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.EMPLOYEE_NOT_FOUND));
        CareerPath careerPath = careerPathRepository.findById(careerPathId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.CAREER_PATH_NOT_FOUND));

        employee.setCareerPath(careerPath);
        employeeRepository.save(employee);
    }

    @Transactional
    public void assignSkillsToCareerPath(Long careerPathId, List<Long> skillIds) {
        CareerPath careerPath = careerPathRepository.findById(careerPathId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.CAREER_PATH_NOT_FOUND));
        List<Skill> skills = skillRepository.findAllById(skillIds);

        careerPath.setSkills(skills);
        careerPathRepository.save(careerPath);
    }

    @Transactional
    public void assignSkillsToEmployee(Long employeeId, Long careerPathId, List<Long> skillIds) {
        assignCareerPathToEmployee(employeeId, careerPathId);
        assignSkillsToCareerPath(careerPathId, skillIds);
    }

    public ResponseEntity<EmployeeResponse> updateEmployee(Long id, EmployeeResponse newEmployeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setFirstName(newEmployeeDetails.getFirstName());
            existingEmployee.setLastName(newEmployeeDetails.getLastName());
            existingEmployee.setPosition(newEmployeeDetails.getPosition());
            existingEmployee.setDepartment(newEmployeeDetails.getDepartment());
            EmployeeResponse updatedEmployee = EmployeeResponse.of(employeeRepository.save(existingEmployee));
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    public ResponseEntity<EmployeeResponse> updateCurrentEmployee(EmployeeResponse newEmployeeDetails) {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"));

        if (newEmployeeDetails.getFirstName() != null) {
            currentEmployee.setFirstName(newEmployeeDetails.getFirstName());
        }
        if (newEmployeeDetails.getLastName() != null) {
            currentEmployee.setLastName(newEmployeeDetails.getLastName());
        }

        if (newEmployeeDetails.getEmail() != null) {
            currentEmployee.setEmail(newEmployeeDetails.getEmail());
        }

        if (newEmployeeDetails.getPosition() != null) {
            currentEmployee.setPosition(newEmployeeDetails.getPosition());
        }
        if (newEmployeeDetails.getDepartment() != null) {
            currentEmployee.setDepartment(newEmployeeDetails.getDepartment());
        }

        EmployeeResponse updatedEmployee = EmployeeResponse.of(employeeRepository.save(currentEmployee));

        return ResponseEntity.ok(updatedEmployee);
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

    @Transactional
    public ResponseEntity<EmployeeResponse> updateCareerLevel(CareerLevel newCareerLevel) {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"));
        currentEmployee.setCareerLevel(newCareerLevel);
        EmployeeResponse updatedEmployee = EmployeeResponse.of(employeeRepository.save(currentEmployee));
        return ResponseEntity.ok(updatedEmployee);
    }

public ResponseEntity<EmployeeResponse> addOwner(Long employeeId, AddOwnerDTO owner) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.getOwners().add(owner);
            EmployeeResponse updatedEmployee = EmployeeResponse.of(employeeRepository.save(employee));
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<EmployeeResponse> addCurrentEmployeeOwner(AddOwnerDTO owner) {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"));
        currentEmployee.getOwners().add(owner);
        EmployeeResponse updatedEmployee = EmployeeResponse.of(employeeRepository.save(currentEmployee));
        return ResponseEntity.ok(updatedEmployee);
    }

    public ResponseEntity<List<Employee>> getCurrentOwnersEmployees() {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"));

        List<Employee> ownersEmployees = new ArrayList<>();
        for (Employee owner : currentEmployee.getOwners()) {
            ownersEmployees.addAll(employeeRepository.findEmployeesByOwnerId(owner.getId()));
        }
        return ResponseEntity.ok(ownersEmployees);
    }
}