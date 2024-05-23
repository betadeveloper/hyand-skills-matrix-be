package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.persistence.entity.Goal;
import com.skillsmatrixapplication.service.GoalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping("/employees/{employeeId}/goals")
    public ResponseEntity<List<Goal>> getEmployeeGoals(@PathVariable Long employeeId) {
        return ResponseEntity.ok(goalService.getEmployeeGoals(employeeId));
    }

    @PostMapping("/goal")
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        return ResponseEntity.ok(goalService.createGoal(goal));
    }

    @PutMapping("/goal/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable Long id, @RequestBody Goal updatedGoal) {
        return ResponseEntity.ok(goalService.updateGoal(id, updatedGoal));
    }

    @DeleteMapping("/goal/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
