package com.rentalmanagement.house_rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rentalmanagement.house_rental")

public class HouseRentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HouseRentalApplication.class, args);
    }
}
