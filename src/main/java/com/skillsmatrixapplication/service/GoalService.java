package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.GoalResponse;
import com.skillsmatrixapplication.persistence.entity.Goal;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class GoalService {

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

        goal.setName(updatedGoal.getName());
        goal.setDescription(updatedGoal.getDescription());
        goal.setDueDate(updatedGoal.getDueDate());
        return goalRepository.save(goal);
    }

    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}
