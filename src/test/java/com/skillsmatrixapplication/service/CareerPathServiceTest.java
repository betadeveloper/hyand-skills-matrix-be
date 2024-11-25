package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CareerPathServiceTest {

    @Mock
    private CareerPathRepository careerPathRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private CareerPathService careerPathService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCareerPaths() {
        CareerPath careerPath1 = new CareerPath(1L, "Developer", "Path for developers", null, null, null);
        CareerPath careerPath2 = new CareerPath(2L, "Manager", "Path for managers", null, null, null);

        when(careerPathRepository.findAll()).thenReturn(List.of(careerPath1, careerPath2));

        List<CareerPath> result = careerPathService.getAllCareerPaths();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(careerPathRepository, times(1)).findAll();
    }

    @Test
    void testGetCareerPathById_Found() {
        CareerPath mockPath = new CareerPath(1L, "Developer", "Path for developers", null, null, null);
        when(careerPathRepository.findById(1L)).thenReturn(Optional.of(mockPath));

        Optional<CareerPath> result = careerPathService.getCareerPathById(1L);
        assertTrue(result.isPresent());
        assertEquals("Developer", result.get().getName());
        verify(careerPathRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCareerPathById_NotFound() {
        when(careerPathRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CareerPath> result = careerPathService.getCareerPathById(1L);
        assertFalse(result.isPresent());
        verify(careerPathRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCareerPath() {
        CareerPath newPath = new CareerPath(null, "New Path", "Description", null, null, null);
        CareerPath savedPath = new CareerPath(1L, "New Path", "Description", null, null, null);

        when(careerPathRepository.save(newPath)).thenReturn(savedPath);

        CareerPath result = careerPathService.createCareerPath(newPath);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(careerPathRepository, times(1)).save(newPath);
    }

    @Test
    void testAssignEmployeesToCareerPath_Success() {
        CareerPath careerPath = new CareerPath(1L, "Developer", "Path for developers", null, null, null);
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setCareerPath(careerPath);
        List<Employee> employees = List.of(employee);

        when(careerPathRepository.findById(1L)).thenReturn(Optional.of(careerPath));
        when(employeeRepository.findAllById(List.of(1L))).thenReturn(employees);
        when(careerPathRepository.save(any(CareerPath.class))).thenReturn(careerPath);

        Optional<CareerPath> result = careerPathService.assignEmployeesToCareerPath(1L, List.of(1L));
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getEmployees().size());
        verify(careerPathRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findAllById(List.of(1L));
        verify(careerPathRepository, times(1)).save(careerPath);
    }

    @Test
    void testAssignEmployeesToCareerPath_EmployeeNotFound() {
        when(careerPathRepository.findById(1L)).thenReturn(Optional.of(new CareerPath()));
        when(employeeRepository.findAllById(List.of(1L))).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> careerPathService.assignEmployeesToCareerPath(1L, List.of(1L))
        );
        assertEquals("Some employees not found.", exception.getMessage());
        verify(careerPathRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findAllById(List.of(1L));
    }

    @Test
    void testAssignEmployeesToCareerPath_EmptyEmployeeList() {
        CareerPath careerPath = new CareerPath(1L, "Developer", "Path for developers", null, null, null);

        when(careerPathRepository.findById(1L)).thenReturn(Optional.of(careerPath));
        when(employeeRepository.findAllById(anyList())).thenReturn(List.of());

        assertThrows(NullPointerException.class, () -> careerPathService.assignEmployeesToCareerPath(1L, List.of()));
        verify(careerPathRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findAllById(anyList());
    }


    @Test
    void testUpdateCareerPath_Success() {
        CareerPath existingPath = new CareerPath(1L, "Developer", "Path for developers", null, null, null);
        CareerPath updatedPath = new CareerPath(1L, "Developer", "Updated Path", null, null, null);

        when(careerPathRepository.findById(1L)).thenReturn(Optional.of(existingPath));
        when(careerPathRepository.save(existingPath)).thenReturn(existingPath);

        Optional<CareerPath> result = careerPathService.updateCareerPath(1L, updatedPath);

        assertTrue(result.isPresent(), "Career path should be updated and returned.");
        assertEquals("Updated Path", result.get().getDescription(), "The description should be updated.");
        verify(careerPathRepository, times(1)).findById(1L);
        verify(careerPathRepository, times(1)).save(existingPath);
    }


    @Test
    void testUpdateCareerPath_NotFound() {
        CareerPath updatedPath = new CareerPath(1L, "Developer", "Updated Path", null, null, null);
        when(careerPathRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CareerPath> result = careerPathService.updateCareerPath(1L, updatedPath);

        assertTrue(result.isEmpty(), "The result should be empty because the career path was not found.");
        verify(careerPathRepository, times(1)).findById(1L);
        verify(careerPathRepository, never()).save(updatedPath);
    }

}
