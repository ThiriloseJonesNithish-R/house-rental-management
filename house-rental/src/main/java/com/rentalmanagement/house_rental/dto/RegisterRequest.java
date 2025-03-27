package com.rentalmanagement.house_rental.dto;

import com.rentalmanagement.house_rental.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}
