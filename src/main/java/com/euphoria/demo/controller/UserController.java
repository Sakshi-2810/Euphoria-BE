package com.euphoria.demo.controller;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.model.Address;
import com.euphoria.demo.security.JwtUtil;
import com.euphoria.demo.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Response> getUserInfo(HttpServletRequest request) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        return ResponseEntity.ok(userDetailsService.getUser(userId));
    }

    @PostMapping("/address")
    public ResponseEntity<Response> addAddress(HttpServletRequest request, @RequestBody Address address) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        return ResponseEntity.ok(userDetailsService.addAddress(userId, address));
    }

    @PutMapping("/address")
    public ResponseEntity<Response> updateAddress(HttpServletRequest request, @RequestBody Address address, @RequestParam String addressId) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        return ResponseEntity.ok(userDetailsService.updateAddress(userId, addressId, address));
    }

    @DeleteMapping("/address")
    public ResponseEntity<Response> deleteAddress(HttpServletRequest request, @RequestParam String addressId) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        return ResponseEntity.ok(userDetailsService.deleteAddress(userId, addressId));
    }

    @GetMapping("/addresses")
    public ResponseEntity<Response> getAddresses(HttpServletRequest request) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        return ResponseEntity.ok(userDetailsService.getAddresses(userId));
    }
}
