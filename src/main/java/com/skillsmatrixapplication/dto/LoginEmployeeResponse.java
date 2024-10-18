package com.skillsmatrixapplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginEmployeeResponse {
    private String token;
    private long expiresIn;
    private List<String> roles;
}