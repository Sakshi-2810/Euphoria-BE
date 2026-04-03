package com.euphoria.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Address {
    @Id
    private String id;
    private String userEmail;
    private String fullName;
    private String phone;
    private String addressLine;
    private String city;
    private String pincode;
    private String state;
    private boolean isDefault;
}