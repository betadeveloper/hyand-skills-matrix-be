package com.skillsmatrixapplication.dto;

import com.skillsmatrixapplication.persistence.entity.Employee;
import lombok.Data;

@Data
public class AddOwnerDTO extends Employee {
    private Long employeeId;
}
