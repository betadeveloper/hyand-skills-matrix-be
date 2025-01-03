package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Skill;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CareerPathRepository careerPathRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void assignCareerPathToEmployee_shouldAssignCareerPath() {
        // Arrange
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee = employeeRepository.save(employee);

        CareerPath careerPath = new CareerPath();
        careerPath.setName("Software Developer");
        careerPath = careerPathRepository.save(careerPath);

        // Act
        employeeService.assignCareerPathToEmployee(employee.getId(), careerPath.getId());

        // Assert
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();
        assertThat(updatedEmployee.getCareerPath().getName()).isEqualTo("Software Developer");
    }

    @Test
    void assignSkillsToCareerPath_shouldAssignSkills() {
        // Arrange
        CareerPath careerPath = new CareerPath();
        careerPath.setName("Software Developer");
        careerPath = careerPathRepository.save(careerPath);

        Skill skill1 = new Skill();
        skill1.setName("Java");
        skillRepository.save(skill1);

        Skill skill2 = new Skill();
        skill2.setName("Spring");
        skillRepository.save(skill2);

        // Act
        employeeService.assignSkillsToCareerPath(careerPath.getId(), List.of(skill1.getId(), skill2.getId()));

        // Assert
        CareerPath updatedCareerPath = careerPathRepository.findById(careerPath.getId()).orElseThrow();
        assertThat(updatedCareerPath.getSkills()).hasSize(2);
        assertThat(updatedCareerPath.getSkills().stream().map(Skill::getName))
                .containsExactlyInAnyOrder("Java", "Spring");
    }

    @Test
    void getEmployeeById_shouldThrowExceptionWhenNotFound() {
        // Assert
        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(999L));
    }
}
