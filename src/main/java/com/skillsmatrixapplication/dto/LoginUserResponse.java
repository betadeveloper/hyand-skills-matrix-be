package com.skillsmatrixapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserResponse {
    private String token;

    private long expiresIn;
}