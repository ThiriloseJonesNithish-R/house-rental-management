package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.dto.AuthRequest;
import com.rentalmanagement.house_rental.dto.AuthResponse;
import com.rentalmanagement.house_rental.dto.RegisterRequest;
import com.rentalmanagement.house_rental.entity.Role;
import com.rentalmanagement.house_rental.entity.User;
import com.rentalmanagement.house_rental.repository.UserRepository;
import com.rentalmanagement.house_rental.utils.JwtUtil;
import com.rentalmanagement.house_rental.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        // Convert role safely
        Role userRole;
        try {
            userRole = Role.valueOf(request.getRole().toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided! Choose either OWNER or TENANT.");
        }

        // Create user and set username to email (since email is our identifier)
        User user = User.builder()
                .username(request.getEmail())  // This line ensures the username is set
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getRole().toString().toUpperCase());
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials! Please check your email or password.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getRole().toString().toUpperCase());
    }
}
