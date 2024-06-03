package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/careerPaths/{id}")
    public ResponseEntity<CareerPath> getCareerPathById(@PathVariable Long id) {
        Optional<CareerPath> optionalCareerPath = careerPathRepository.findById(id);
        return optionalCareerPath.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/careerPaths/{id}/employees")
    public ResponseEntity<List<Employee>> getCareerPathEmployees(@PathVariable Long id) {
        return careerPathRepository.getCareerPathEmployees(id);
    }

    @PostMapping("/careerPaths")
    public ResponseEntity<CareerPath> createCareerPath(@RequestBody CareerPath careerPath) {
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
