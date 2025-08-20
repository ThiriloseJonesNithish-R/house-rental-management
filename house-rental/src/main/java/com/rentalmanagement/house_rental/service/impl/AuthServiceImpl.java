package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.dto.AuthRequest;
import com.rentalmanagement.house_rental.dto.AuthResponse;
import com.rentalmanagement.house_rental.dto.RegisterRequest;
import com.rentalmanagement.house_rental.dto.MessageResponse; // ✅ Import the new DTO
import com.rentalmanagement.house_rental.entity.Role;
import com.rentalmanagement.house_rental.entity.User;
import com.rentalmanagement.house_rental.repository.UserRepository;
import com.rentalmanagement.house_rental.utils.JwtUtil;
import com.rentalmanagement.house_rental.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.rentalmanagement.house_rental.entity.Tenant;
import com.rentalmanagement.house_rental.entity.Owner;
import com.rentalmanagement.house_rental.repository.TenantRepository;
import com.rentalmanagement.house_rental.repository.OwnerRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TenantRepository tenantRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public MessageResponse register(RegisterRequest request) { // ✅ Updated return type
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered!");
        }

        // Validate role
        Role userRole;
        try {
            userRole = Role.valueOf(request.getRole().toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role! Choose either OWNER or TENANT.");
        }

        // Create user
        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .contact(request.getContact()) // ✅ Moved contact to User entity
                .build();

        userRepository.save(user);

        // Create tenant or owner based on role
        if (userRole == Role.TENANT) {
            Tenant tenant = new Tenant();
            tenant.setUser(user);
            tenant.setName(request.getName());
            tenant.setPreferredLocation(request.getPreferredLocation());
            tenantRepository.save(tenant);
        } else if (userRole == Role.OWNER) {
            Owner owner = new Owner();
            owner.setUser(user);
            //owner.setName(request.getName());
            // If request.getName() is null/empty, fallback to email
    String ownerName = (request.getName() == null || request.getName().isBlank())
            ? request.getEmail()
            : request.getName();
            owner.setName(ownerName);
            owner.setIsActive(true); // Set owner as active by default
            // Save the owner entity
            ownerRepository.save(owner);
        }

        // ✅ Return a success message instead of a token
        String successMessage = (userRole == Role.OWNER) ? "Owner registered successfully!"
                : "Tenant registered successfully!";

        return new MessageResponse(successMessage);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials! Please check your email or password.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        String role = user.getRole().toString().toUpperCase();
        String message = role + " logged in successfully!"; // ✅ Add login success message

        return new AuthResponse(token, role, message);
    }

}
