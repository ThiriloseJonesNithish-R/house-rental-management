package com.rentalmanagement.house_rental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthResponse {
    private String token;
    private String role;
    private String message; // ✅ Added success message
}
