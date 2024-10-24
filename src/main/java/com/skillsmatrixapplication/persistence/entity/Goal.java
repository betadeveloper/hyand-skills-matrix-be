package com.skillsmatrixapplication.persistence.entity;

import com.skillsmatrixapplication.enums.GoalStatusEnum;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @Nullable
    private LocalDate startDate;

    @Column
    private LocalDate dueDate;

    @Column
    @Nullable
    private LocalDate endDate;

    @Column
    private GoalStatusEnum status;

    @Column(name = "employee_id")
    private Long employeeId;
}
