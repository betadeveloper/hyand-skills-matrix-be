package com.skillsmatrixapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFeedbackRequest {
    private Long ownerId;
    private Long employeeId;
    private String feedbackText;
}
