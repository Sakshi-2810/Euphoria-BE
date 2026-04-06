package com.euphoria.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
    private String token;
}
