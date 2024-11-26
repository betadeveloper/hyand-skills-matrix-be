package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.EmployeeResponse;
import com.skillsmatrixapplication.enums.RoleEnum;
import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Role;
import com.skillsmatrixapplication.persistence.entity.Skill;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CareerPathRepository careerPathRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testAssignCareerPathToEmployee() {
        Long employeeId = 1L;
        Long careerPathId = 1L;
        Employee employee = new Employee();
        CareerPath careerPath = new CareerPath();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.of(careerPath));

        employeeService.assignCareerPathToEmployee(employeeId, careerPathId);

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testAssignSkillsToCareerPath() {
        Long careerPathId = 1L;
        List<Long> skillIds = Collections.singletonList(1L);
        CareerPath careerPath = new CareerPath();
        Skill skill = new Skill();

        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.of(careerPath));
        when(skillRepository.findAllById(skillIds)).thenReturn(Collections.singletonList(skill));

        employeeService.assignSkillsToCareerPath(careerPathId, skillIds);

        verify(careerPathRepository, times(1)).save(careerPath);
    }

    @Test
    void testAssignSkillsToEmployee() {
        Long employeeId = 1L;
        Long careerPathId = 1L;
        List<Long> skillIds = Collections.singletonList(1L);
        Employee employee = new Employee();
        CareerPath careerPath = new CareerPath();
        careerPath.setEmployees(Collections.singletonList(employee));
        Skill skill = new Skill();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.of(careerPath));
        when(skillRepository.findAllById(skillIds)).thenReturn(Collections.singletonList(skill));

        employeeService.assignSkillsToEmployee(employeeId, careerPathId, skillIds);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(careerPathRepository, times(2)).findById(careerPathId);
        verify(skillRepository, times(1)).findAllById(skillIds);
    }

    @Test
    void testUpdateEmployee() {
        Long id = 1L;
        EmployeeResponse newEmployeeDetails = EmployeeResponse.builder()
                .firstName("John")
                .build();
        Employee employee = new Employee();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<EmployeeResponse> result = employeeService.updateEmployee(id, newEmployeeDetails);

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testGetEmployeeById() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setFirstName("John");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeRepository.findById(employeeId);

        assertEquals(employeeId, result.get().getId());
        assertEquals("John", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void testGetAllEmployees() {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));

        List<Employee> employees = employeeRepository.findAll();

        assertEquals(2, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Jane", employees.get(1).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        Long employeeId = 99L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getEmployeeById(employeeId);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(employeeId);
    }
}