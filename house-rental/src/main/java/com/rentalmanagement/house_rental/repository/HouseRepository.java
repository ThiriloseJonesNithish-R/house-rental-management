package com.rentalmanagement.house_rental.repository;

import com.rentalmanagement.house_rental.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByLocation(String location);

    List<House> findByLocationAndPriceLessThanEqual(String location, double maxPrice);

    Optional<House> findByLocationAndPriceAndOwner(String location, double price, String owner);
}
