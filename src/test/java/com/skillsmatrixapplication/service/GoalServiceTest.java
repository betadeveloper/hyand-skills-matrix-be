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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testGetEmployeeGoals() {
        Employee employee = new Employee();
        Long employeeId = 1L;
        employee.setId(employeeId);
        Goal goal = new Goal();
        goal.setEmployee(employee);

        when(goalRepository.findAll()).thenReturn(Collections.singletonList(goal));

        List<GoalResponse> result = goalService.getEmployeeGoals(employeeId);

        assertEquals(1, result.size());
        verify(goalRepository, times(1)).findAll();
    }

    @Test
    void testUpdateGoal() {
        Goal goal = new Goal();
        Long goalId = 1L;
        goal.setId(goalId);

        when(goalRepository.findById(anyLong())).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.updateGoal(goalId, goal);

        assertEquals(goal, result);
    }

    @Test
    void testDeleteGoal() {
        Long goalId = 1L;

        doNothing().when(goalRepository).deleteById(anyLong());

        goalService.deleteGoal(goalId);

        verify(goalRepository, times(1)).deleteById(goalId);
    }
}