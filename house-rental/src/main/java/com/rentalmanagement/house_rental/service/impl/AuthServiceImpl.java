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
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Convert role String to enum safely
        try {
            user.setRole(Role.valueOf(request.getRole().toString().toUpperCase()));

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided!");
        }

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getRole().name());
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }

        String token = jwtUtil.generateToken(user.getEmail()); // ✅ Fix generateToken issue
        return new AuthResponse(token, user.getRole().name()); // ✅ Ensure correct response format
    }
}
