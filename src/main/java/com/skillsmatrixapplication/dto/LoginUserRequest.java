package com.skillsmatrixapplication.dto;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
