package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.GoalResponse;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Goal;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    void testGetEmployeeGoals_success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        Goal goal = new Goal();
        goal.setId(1L);
        goal.setDescription("Goal 1");
        goal.setEmployeeId(1L);


        when(goalRepository.findAll()).thenReturn(Collections.singletonList(goal));

        List<GoalResponse> result = goalService.getEmployeeGoals(employeeId);

        assertEquals(1, result.size());
        assertEquals("Goal 1", result.get(0).getDescription());
        verify(goalRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeGoals_noGoalsFound() {
        Long employeeId = 1L;

        when(goalRepository.findAll()).thenReturn(Collections.emptyList());

        List<GoalResponse> result = goalService.getEmployeeGoals(employeeId);

        assertTrue(result.isEmpty());
        verify(goalRepository, times(1)).findAll();
    }

    @Test
    void testUpdateGoal_success() {
        Long goalId = 1L;
        Goal existingGoal = new Goal();
        existingGoal.setId(goalId);
        existingGoal.setDescription("Old Description");

        Goal updatedGoal = new Goal();
        updatedGoal.setId(goalId);
        updatedGoal.setDescription("New Description");

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(existingGoal));
        when(goalRepository.save(existingGoal)).thenReturn(updatedGoal);

        Goal result = goalService.updateGoal(goalId, updatedGoal);

        assertNotNull(result);
        assertEquals("New Description", result.getDescription());
        verify(goalRepository, times(1)).findById(goalId);
        verify(goalRepository, times(1)).save(existingGoal);
    }

    @Test
    void testUpdateGoal_goalNotFound() {
        Long goalId = 1L;
        Goal updatedGoal = new Goal();

        when(goalRepository.findById(goalId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> goalService.updateGoal(goalId, updatedGoal));
        assertEquals("Goal not found", exception.getMessage());

        verify(goalRepository, times(1)).findById(goalId);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void testDeleteGoal_success() {
        Long goalId = 1L;

        when(goalRepository.existsById(goalId)).thenReturn(true);
        doNothing().when(goalRepository).deleteById(goalId);

        goalService.deleteGoal(goalId);

        verify(goalRepository, times(1)).existsById(goalId);
        verify(goalRepository, times(1)).deleteById(goalId);
    }


    @Test
    void testDeleteGoal_nullGoalId() {
        assertThrows(IllegalArgumentException.class, () -> goalService.deleteGoal(null));
        verify(goalRepository, never()).deleteById(any());
    }

    @Test
    void testGetEmployeeGoals_employeeDoesNotExist() {
        Long employeeId = 1L;

        when(goalRepository.findAll()).thenReturn(Collections.emptyList());

        List<GoalResponse> result = goalService.getEmployeeGoals(employeeId);

        assertTrue(result.isEmpty());
        verify(goalRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeGoals_nullEmployeeId() {
        List<GoalResponse> result = goalService.getEmployeeGoals(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateGoal_nullGoal() {
        Long goalId = 1L;

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(new Goal()));

        assertThrows(NullPointerException.class, () -> goalService.updateGoal(goalId, null));
        verify(goalRepository, never()).save(any(Goal.class));
    }
}
