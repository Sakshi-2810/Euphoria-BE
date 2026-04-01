package com.euphoria.demo.controller;

import com.euphoria.demo.dto.LoginDto;
import com.euphoria.demo.dto.Response;
import com.euphoria.demo.dto.SignupDto;
import com.euphoria.demo.repository.UserRepository;
import com.euphoria.demo.security.JwtUtil;
import com.euphoria.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginDto request) {
        return ResponseEntity.ok(userDetailsService.login(request));
    }

    // ✅ 3. OAUTH LOGIN (Already exists)
    @PostMapping("/oauth-login")
    public ResponseEntity<Response> oauthLogin(@RequestBody LoginDto request) {
        Response response = userDetailsService.loginViaOAuth(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody SignupDto request) {
        return ResponseEntity.ok(userDetailsService.signup(request));
    }
}