package com.euphoria.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class User {
    private String name;
    @Id
    private String email;
    private String phone;
    private String password;
    private String role;
}
