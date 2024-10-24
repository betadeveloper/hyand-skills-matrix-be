package com.skillsmatrixapplication.dto;

import com.skillsmatrixapplication.enums.GoalStatusEnum;
import com.skillsmatrixapplication.persistence.entity.Goal;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GoalResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private GoalStatusEnum status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate endDate;
    private Long employeeId;

    public static GoalResponse of(final Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .name(goal.getName())
                .description(goal.getDescription())
                .status(goal.getStatus())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .dueDate(goal.getDueDate())
                .employeeId(goal.getEmployeeId())
                .build();
    }
}
