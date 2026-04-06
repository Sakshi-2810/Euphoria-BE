package com.euphoria.demo.service;

import com.euphoria.demo.dto.LoginDto;
import com.euphoria.demo.dto.Response;
import com.euphoria.demo.dto.SignupDto;
import com.euphoria.demo.exception.CustomDataException;
import com.euphoria.demo.model.Address;
import com.euphoria.demo.model.User;
import com.euphoria.demo.repository.AddressRepository;
import com.euphoria.demo.repository.UserRepository;
import com.euphoria.demo.security.CustomUserDetails;
import com.euphoria.demo.security.JwtUtil;
import com.euphoria.demo.utils.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    GoogleUtils googleUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new CustomUserDetails(user);
    }

    public Response loginViaOAuth(String token) {
        try {
            GoogleIdToken.Payload payload = googleUtils.verifyGoogleToken(token);
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            User user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword("");
                user.setRole("USER");
                userRepository.save(user);
            }
            log.info("User logged in via OAuth: {}", email);
            return new Response("Login successful", Map.of("token", jwtUtil.generateToken(user.getEmail(), "USER"), "user", user));
        } catch (Exception e) {
            log.error("OAuth login failed: {}", e.getMessage());
            throw new CustomDataException("Invalid OAuth token");
        }
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

    public Response getUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new Response("User not found", null);
        }
        log.info("Fetched user: {}", email);
        return new Response("Addresses fetched successfully", user);
    }

    public Response addAddress(String email, Address address) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new Response("User not found", null);
        }
        address.setUserEmail(email);
        addressRepository.save(address);
        log.info("Added address for user: {}", email);
        return new Response("Address added successfully", address);
    }

    public Response getAddresses(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new Response("User not found", null);
        }
        List<Address> addresses = addressRepository.findByUserEmail(email);
        log.info("Fetched addresses for user: {}", email);
        return new Response("Addresses fetched successfully", addresses);
    }

    public Response updateAddress(String email, String addressId, Address updatedAddress) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new Response("User not found", null);
        }
        Address existingAddress = addressRepository.findById(addressId).orElse(null);
        if (existingAddress == null || !existingAddress.getUserEmail().equals(email)) {
            return new Response("Address not found or does not belong to user", null);
        }
        updatedAddress.setId(addressId);
        updatedAddress.setUserEmail(email);
        addressRepository.save(updatedAddress);
        log.info("Updated address for user: {}", email);
        return new Response("Address updated successfully", updatedAddress);
    }

    public Response deleteAddress(String email, String addressId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new Response("User not found", null);
        }
        Address existingAddress = addressRepository.findById(addressId).orElse(null);
        if (existingAddress == null || !existingAddress.getUserEmail().equals(email)) {
            return new Response("Address not found or does not belong to user", null);
        }
        addressRepository.deleteById(addressId);
        log.info("Deleted address for user: {}", email);
        return new Response("Address deleted successfully", addressId);
    }
}