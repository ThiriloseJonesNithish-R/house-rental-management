package com.rentalmanagement.house_rental.dto;

import lombok.Data;

@Data
public class TenantDTO {
    private Long id;
    private String name;
    private String contact;
    private String preferredLocation;
}
