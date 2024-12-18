package com.skillsmatrixapplication.dto;

import com.skillsmatrixapplication.enums.CareerLevel;
import com.skillsmatrixapplication.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String reviewReport;
    private Double score;
    private CareerLevel careerLevel;
    private CareerLevel evaluatedCareerLevel;
    private EmployeeResponse employee;
    private EmployeeResponse owner;
    private LocalDate reviewDate;
    private ReviewStatus status;
}
