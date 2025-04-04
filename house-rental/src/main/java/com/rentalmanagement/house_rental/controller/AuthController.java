package com.rentalmanagement.house_rental.controller;

import com.rentalmanagement.house_rental.dto.AuthRequest;
import com.rentalmanagement.house_rental.dto.AuthResponse;
import com.rentalmanagement.house_rental.dto.RegisterRequest;
import com.rentalmanagement.house_rental.dto.MessageResponse; // ✅ Import the new DTO
import com.rentalmanagement.house_rental.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*") // ✅ Allow all origins temporarily for testing
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) { // ✅ Updated return type
        System.out.println("✅ Register API called: " + request);
        MessageResponse response = authService.register(request);
        System.out.println("✅ Register API response: " + response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("✅ Login API called: " + request);
        AuthResponse response = authService.login(request);
        System.out.println("✅ Login API response: " + response);
        return ResponseEntity.ok(response);
    }
}
