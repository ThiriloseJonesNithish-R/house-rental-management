package com.rentalmanagement.house_rental.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HouseDTO {
    private Long id;
    private String location;
    private double price;
    private int bedrooms;

    // ✅ Only for responses, not required in requests
    private String ownerName;
}
