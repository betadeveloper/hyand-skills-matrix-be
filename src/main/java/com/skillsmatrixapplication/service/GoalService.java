package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.GoalResponse;
import com.skillsmatrixapplication.persistence.entity.Goal;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class GoalService {

    private static final Logger logger = LoggerFactory.getLogger(GoalService.class);

    private final GoalRepository goalRepository;
    private final EmployeeRepository employeeRepository;

    public List<GoalResponse> getEmployeeGoals(Long employeeId) {
        return goalRepository.findAll().stream()
                .filter(goal -> goal.getEmployeeId().equals(employeeId))  // Compare using employeeId
                .map(GoalResponse::of)
                .toList();
    }

    public List<GoalResponse> getCurrentEmployeeGoals() {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentEmployeeId = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"))
                .getId();

        return goalRepository.findAll().stream()
                .filter(goal -> goal.getEmployeeId().equals(currentEmployeeId))
                .map(GoalResponse::of)
                .toList();
    }

    public Goal createGoal(Goal goal) {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentEmployeeId = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Current employee not found"))
                .getId();

        goal.setEmployeeId(currentEmployeeId);
        return goalRepository.save(goal);
    }


    public Goal updateGoal(Long id, Goal updatedGoal) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        logger.info("Updating goal with ID: {}", id);
        logger.info("Updated Goal Data - Name: {}, Description: {}, Due Date: {}, Start Date: {}, End Date: {}",
                updatedGoal.getName(), updatedGoal.getDescription(), updatedGoal.getDueDate(),
                updatedGoal.getStartDate(), updatedGoal.getEndDate());

        goal.setName(updatedGoal.getName());
        goal.setDescription(updatedGoal.getDescription());
        goal.setDueDate(updatedGoal.getDueDate());

        if (updatedGoal.getStartDate() != null) {
            goal.setStartDate(updatedGoal.getStartDate());
        }
        if (updatedGoal.getEndDate() != null) {
            goal.setEndDate(updatedGoal.getEndDate());
        }

        return goalRepository.save(goal);
    }


    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}
