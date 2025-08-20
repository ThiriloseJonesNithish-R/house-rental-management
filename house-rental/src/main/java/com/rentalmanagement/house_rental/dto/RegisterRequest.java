package com.rentalmanagement.house_rental.dto;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {
    private String email;
    private String password;
    private String role;

    private String name;
    private String contact; // ✅ This now belongs to the User
    private String preferredLocation; // Only used if TENANT
}
