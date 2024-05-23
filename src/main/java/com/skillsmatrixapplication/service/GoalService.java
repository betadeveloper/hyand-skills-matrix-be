package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.Goal;
import com.skillsmatrixapplication.persistence.repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    public List<Goal> getEmployeeGoals(Long employeeId) {
        return goalRepository.findAll().stream()
                .filter(goal -> goal.getEmployee().getId().equals(employeeId))
                .collect(Collectors.toList());
    }

    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public Goal updateGoal(Long id, Goal updatedGoal) {
        Goal goal = goalRepository.findById(id).orElseThrow();
        goal.setName(updatedGoal.getName());
        goal.setDescription(updatedGoal.getDescription());
        goal.setDueDate(updatedGoal.getDueDate());
        return goalRepository.save(goal);
    }

    public void deleteGoal(Long id) {
            goalRepository.deleteById(id);
    }
}
