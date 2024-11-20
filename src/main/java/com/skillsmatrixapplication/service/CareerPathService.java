package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareerPathService {

    private final CareerPathRepository careerPathRepository;
    private final EmployeeRepository employeeRepository;

    public CareerPathService(CareerPathRepository careerPathRepository, EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.careerPathRepository = careerPathRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<CareerPath> getAllCareerPaths() {
        return careerPathRepository.findAll();
    }

    public Optional<CareerPath> getCareerPathById(Long id) {
        return careerPathRepository.findById(id);
    }

    public Optional<CareerPath> getCurrentEmployeeCareerPath() {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.ofNullable(employeeRepository.getCurrentEmployeeCareerPath(currentEmployeeEmail));
    }

    public CareerPath createCareerPath(CareerPath careerPath) {
        return careerPathRepository.save(careerPath);
    }

    public Optional<CareerPath> updateCareerPath(Long id, CareerPath newCareerPathDetails) {
        return careerPathRepository.findById(id).map(careerPath -> {
            careerPath.setName(newCareerPathDetails.getName());
            careerPath.setDescription(newCareerPathDetails.getDescription());
            careerPath.setSkills(newCareerPathDetails.getSkills());
            return careerPathRepository.save(careerPath);
        });
    }

    public void deleteCareerPath(Long id) {
        careerPathRepository.deleteById(id);
    }

    public Optional<CareerPath> assignEmployeesToCareerPath(Long id, List<Long> employeeIds) {
        Optional<CareerPath> careerPathOpt = careerPathRepository.findById(id);
        if (careerPathOpt.isEmpty()) {
            return Optional.empty();
        }

        CareerPath careerPath = careerPathOpt.get();
        List<Employee> employees = employeeRepository.findAllById(employeeIds);

        if (employees.size() != employeeIds.size()) {
            throw new IllegalArgumentException("Some employees not found.");
        }

        careerPath.setEmployees(employees);
        return Optional.of(careerPathRepository.save(careerPath));
    }

    public List<Employee> getEmployeesForCareerPath(Long id) {
        Optional<CareerPath> careerPathOpt = careerPathRepository.findById(id);
        if (careerPathOpt.isPresent()) {
            return careerPathOpt.get().getEmployees();
        }
        return List.of();
    }
}
