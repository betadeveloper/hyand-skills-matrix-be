package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.CreateCareerPathRequest;
import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.service.CareerPathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CareerPathController {

    private final CareerPathService careerPathService;

    public CareerPathController(CareerPathService careerPathService) {
        this.careerPathService = careerPathService;
    }

    @GetMapping("/careerPaths/all")
    public ResponseEntity<List<CareerPath>> getAllCareerPaths() {
        return ResponseEntity.ok(careerPathService.getAllCareerPaths());
    }

    @GetMapping("/careerPaths/{id}")
    public ResponseEntity<CareerPath> getCareerPathById(@PathVariable Long id) {
        Optional<CareerPath> careerPath = careerPathService.getCareerPathById(id);
        return careerPath.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/careerPaths/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesForCareerPath(@PathVariable Long id) {
        List<Employee> employees = careerPathService.getEmployeesForCareerPath(id);
        return employees.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(employees);
    }

    @PostMapping("/careerPaths")
    public ResponseEntity<CareerPath> createCareerPath(@RequestBody CreateCareerPathRequest request) {
        CareerPath careerPath = new CareerPath();
        careerPath.setName(request.getName());
        careerPath.setDescription(request.getDescription());
        return ResponseEntity.ok(careerPathService.createCareerPath(careerPath));
    }

    @PutMapping("/careerPaths/{id}")
    public ResponseEntity<CareerPath> updateCareerPath(@PathVariable Long id, @RequestBody CareerPath newCareerPathDetails) {
        return careerPathService.updateCareerPath(id, newCareerPathDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/careerPaths/{id}")
    public ResponseEntity<Void> deleteCareerPath(@PathVariable Long id) {
        careerPathService.deleteCareerPath(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/careerPaths/{id}/assignEmployees")
    public ResponseEntity<CareerPath> assignEmployeesToCareerPath(
            @PathVariable Long id,
            @RequestBody List<Long> employeeIds) {
        try {
            Optional<CareerPath> updatedCareerPath = careerPathService.assignEmployeesToCareerPath(id, employeeIds);
            return updatedCareerPath.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
