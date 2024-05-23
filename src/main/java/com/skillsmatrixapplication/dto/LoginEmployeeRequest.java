package com.skillsmatrixapplication.dto;

import lombok.Data;

@Data
public class LoginEmployeeRequest {
    private String email;
    private String password;
}
