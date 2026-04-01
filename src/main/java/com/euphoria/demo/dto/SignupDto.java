package com.euphoria.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDto {
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String phone;
}
