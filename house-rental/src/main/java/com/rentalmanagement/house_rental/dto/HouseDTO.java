package com.rentalmanagement.house_rental.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseDTO {
    private Long id;
    private String location;
    private double price;
    private int bedrooms;
    private String owner;
}
