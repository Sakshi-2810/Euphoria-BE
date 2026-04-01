package com.euphoria.demo.service;

import com.euphoria.demo.dto.LoginDto;
import com.euphoria.demo.dto.Response;
import com.euphoria.demo.dto.SignupDto;
import com.euphoria.demo.model.User;
import com.euphoria.demo.repository.UserRepository;
import com.euphoria.demo.security.CustomUserDetails;
import com.euphoria.demo.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new CustomUserDetails(user);
    }

    public Response loginViaOAuth(String email) {
        // 1. Check if user exists
        User user = userRepository.findByEmail(email);
        // 2. If NOT exists → create new user
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName("New User");
            user.setPassword(""); // optional (or generate random)
            user.setRole("USER");
            user.setAddresses(Collections.emptyList());

            userRepository.save(user);
        }
        log.info("User logged in via OAuth: {}", email);
        return new Response("Login successful", jwtUtil.generateToken(user.getEmail(), "USER"));
    }

    public Response signup(SignupDto request) {

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return new Response("This email already exists", null);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐 hash
        user.setRole("USER");
        user.setAddresses(Collections.emptyList());

        userRepository.save(user);

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        log.info("New user signed up: {}", request.getEmail());
        return new Response("Signup successful", Map.of("token", token, "user", user));
    }

    public Response login(LoginDto request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new Response("Invalid email or password", null);
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        log.info("User logged in: {}", request.getEmail());
        return new Response("Login successful", Map.of("token", token, "user", user));
    }

    public Response saveAddress(User.Address address, String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new Response("User not found", null);
        }
        user.getAddresses().add(address);
        userRepository.save(user);
        log.info("Address added for user: {}", email);
        return new Response("Address saved successfully", user.getAddresses());
    }
}