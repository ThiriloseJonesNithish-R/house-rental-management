package com.rentalmanagement.house_rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  // ✅ Ensures the constructor exists
public class AuthResponse {
    private String token;
    private String role;
}
