package com.skillsmatrixapplication.dto;

import com.skillsmatrixapplication.model.enums.CareerLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private CareerLevel careerLevel;
    private String department;
    private Date createdAt;
    private Date updatedAt;
    private List<String> roles;
}