package com.skillsmatrixapplication.dto;

import com.skillsmatrixapplication.enums.RoleEnum;
import com.skillsmatrixapplication.enums.CareerLevel;
import com.skillsmatrixapplication.persistence.entity.Employee;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
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
    private List<RoleEnum> roles;

    public static EmployeeResponse of(final Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .careerLevel(employee.getCareerLevel())
                .department(employee.getDepartment())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .roles(employee.getRoles())
                .build();
    }
}
