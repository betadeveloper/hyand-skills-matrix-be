package com.skillsmatrixapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {
    private String careerLevel;
    private String evaluatedCareerLevel;
    private String reviewText;
    private Double score;
}
