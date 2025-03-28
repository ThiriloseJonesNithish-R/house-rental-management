package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.dto.AuthResponse;
import com.rentalmanagement.house_rental.dto.AuthRequest;
import com.rentalmanagement.house_rental.dto.RegisterRequest;
import com.rentalmanagement.house_rental.dto.MessageResponse; // ✅ Import the new DTO

public interface AuthService {
    MessageResponse register(RegisterRequest request); // ✅ Change return type
    AuthResponse login(AuthRequest request);
}
