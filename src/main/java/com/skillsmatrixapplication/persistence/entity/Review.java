package com.skillsmatrixapplication.persistence.entity;

import com.skillsmatrixapplication.enums.CareerLevel;
import com.skillsmatrixapplication.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reviewDate;

    private String reviewText;

    private Double score;
    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;
    @Enumerated(EnumType.STRING)
    private CareerLevel evaluatedCareerLevel;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Employee owner;
}