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
    private List<Address> addresses;
    private String role;

    @Data
    public static class Address {
        private String fullName;
        private String phone;
        private String addressLine;
        private String city;
        private String pincode;
        private String state;
    }
}
