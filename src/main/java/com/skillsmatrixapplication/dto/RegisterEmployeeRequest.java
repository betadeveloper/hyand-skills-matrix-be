package com.skillsmatrixapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
