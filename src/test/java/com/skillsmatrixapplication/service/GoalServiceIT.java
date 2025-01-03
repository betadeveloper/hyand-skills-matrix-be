package com.skillsmatrixapplication.integration;

import com.skillsmatrixapplication.dto.GoalResponse;
import com.skillsmatrixapplication.persistence.entity.Goal;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.GoalRepository;
import com.skillsmatrixapplication.service.GoalService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GoalServiceIT {

    @Autowired
    private GoalService goalService;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        goalRepository.deleteAll();
    }

    @Test
    void testCreateGoal() {
        Goal goal = new Goal();
        goal.setName("Learn Spring Boot");
        goal.setDescription("Complete Spring Boot tutorials");
        goal.setDueDate(LocalDate.now().plusMonths(1));

        Goal createdGoal = goalService.createGoal(goal);

        assertNotNull(createdGoal.getId());
        assertEquals("Learn Spring Boot", createdGoal.getName());
        assertEquals("Complete Spring Boot tutorials", createdGoal.getDescription());
    }

    @Test
    void testGetEmployeeGoals() {
        Goal goal1 = new Goal();
        goal1.setName("Goal 1");
        goal1.setDescription("Description 1");
        goal1.setEmployeeId(1L);
        goalRepository.save(goal1);

        Goal goal2 = new Goal();
        goal2.setName("Goal 2");
        goal2.setDescription("Description 2");
        goal2.setEmployeeId(2L);
        goalRepository.save(goal2);

        List<GoalResponse> employeeGoals = goalService.getEmployeeGoals(1L);

        assertEquals(1, employeeGoals.size());
        assertEquals("Goal 1", employeeGoals.get(0).getName());
    }

    @Test
    void testUpdateGoal() {
        Goal goal = new Goal();
        goal.setName("Initial Goal");
        goal.setDescription("Initial Description");
        goal.setDueDate(LocalDate.now().plusWeeks(2));
        Goal savedGoal = goalRepository.save(goal);

        Goal updatedGoal = new Goal();
        updatedGoal.setName("Updated Goal");
        updatedGoal.setDescription("Updated Description");
        updatedGoal.setDueDate(LocalDate.now().plusMonths(1));

        Goal result = goalService.updateGoal(savedGoal.getId(), updatedGoal);

        assertEquals("Updated Goal", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(updatedGoal.getDueDate(), result.getDueDate());
    }

    @Test
    void testDeleteGoal() {
        Goal goal = new Goal();
        goal.setName("To Be Deleted");
        goal.setDescription("This goal will be deleted.");
        Goal savedGoal = goalRepository.save(goal);

        goalService.deleteGoal(savedGoal.getId());

        assertFalse(goalRepository.existsById(savedGoal.getId()));
    }

    @Test
    void testGetCurrentEmployeeGoals_NoGoals() {
        List<GoalResponse> goals = goalService.getCurrentEmployeeGoals();
        assertTrue(goals.isEmpty());
    }

    @Test
    void testGetEmployeeGoals_InvalidEmployee() {
        List<GoalResponse> goals = goalService.getEmployeeGoals(999L);
        assertTrue(goals.isEmpty());
    }

    @Test
    void testCreateGoal_InvalidData() {
        Goal goal = new Goal();
        goal.setName(null);  // Invalid data

        assertThrows(IllegalArgumentException.class, () -> goalService.createGoal(goal));
    }

    @Test
    void testUpdateGoal_NotFound() {
        Goal goal = new Goal();
        goal.setName("Nonexistent Goal");
        goal.setDescription("Does not exist.");

        assertThrows(RuntimeException.class, () -> goalService.updateGoal(999L, goal));
    }

    @Test
    void testDeleteGoal_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> goalService.deleteGoal(999L));
    }

    @Test
    void testDeleteGoal_NullId() {
        assertThrows(IllegalArgumentException.class, () -> goalService.deleteGoal(null));
    }
}
