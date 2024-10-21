package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.CreateCareerPathRequest;
import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CareerPathController {

    private final CareerPathRepository careerPathRepository;

    public CareerPathController(CareerPathRepository careerPathRepository) {
        this.careerPathRepository = careerPathRepository;
    }

    @GetMapping("/careerPaths")
    public ResponseEntity<List<CareerPath>> getCareerPaths() {
        List<CareerPath> careerPaths = careerPathRepository.findAll();
        return ResponseEntity.ok(careerPaths);
    }

    // get current employee career paths
//    @GetMapping("/employee/current/careerPaths")
//    public ResponseEntity<Optional<CareerPath>> getCurrentEmployeeCareerPaths() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentEmployeeEmail = authentication.getName();
//
//        Optional<CareerPath> careerPaths = careerPathRepository.findByEmployeeEmail(currentEmployeeEmail);
//        return ResponseEntity.ok(careerPaths);
//    }

    @GetMapping("/careerPaths/{id}")
    public ResponseEntity<CareerPath> getCareerPathById(@PathVariable Long id) {
        Optional<CareerPath> optionalCareerPath = careerPathRepository.findById(id);
        return optionalCareerPath.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/careerPaths/{id}/employees")
    public ResponseEntity<List<Employee>> getCareerPathEmployees(@PathVariable Long id) {
        return careerPathRepository.getCareerPathEmployees(id);
    }

    @GetMapping("/careerPaths/all")
    public ResponseEntity<List<CareerPath>> getAllCareerPaths() {
        List<CareerPath> careerPaths = careerPathRepository.findAll();
        return ResponseEntity.ok(careerPaths);
    }


    @PostMapping("/careerPaths")
    public ResponseEntity<CareerPath> createCareerPath(@RequestBody CreateCareerPathRequest request) {
        CareerPath careerPath = new CareerPath();
        careerPath.setName(request.getName());
        careerPath.setDescription(request.getDescription());
        CareerPath savedCareerPath = careerPathRepository.save(careerPath);
        return ResponseEntity.ok(savedCareerPath);
    }

    @PutMapping("/careerPaths/{id}")
    public ResponseEntity<CareerPath> updateCareerPath(@PathVariable Long id, @RequestBody CareerPath newCareerPathDetails) {
        return careerPathRepository.findById(id)
                .map(careerPath -> {
                    careerPath.setName(newCareerPathDetails.getName());
                    careerPath.setScore(newCareerPathDetails.getScore());
                    careerPath.setSkills(newCareerPathDetails.getSkills());
                    CareerPath updatedCareerPath = careerPathRepository.save(careerPath);
                    return ResponseEntity.ok(updatedCareerPath);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
