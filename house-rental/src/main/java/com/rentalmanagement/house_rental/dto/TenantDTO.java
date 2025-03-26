package com.rentalmanagement.house_rental.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO {
    private Long id;
    private String name;
    private String contact;
    private String preferredLocation;
}
