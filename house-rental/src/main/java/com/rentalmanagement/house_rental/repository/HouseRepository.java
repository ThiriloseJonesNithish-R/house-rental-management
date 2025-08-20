package com.rentalmanagement.house_rental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rentalmanagement.house_rental.entity.House;
import com.rentalmanagement.house_rental.entity.Owner;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    
    // ❌ Removed `findByLocation()` (Replaced with searchByLocation)
    
    // ✅ New method (Flexible search, case-insensitive, keyword support)
    @Query("SELECT h FROM House h WHERE LOWER(REPLACE(h.location, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:location, ' ', ''), '%'))")
    List<House> searchByLocation(@Param("location") String location);

    // ❌ Removed `findByLocationAndPriceLessThanEqual()` (Replaced with `searchByLocationAndPrice`)

    // ✅ Case-insensitive location + price search (Handles spaces)
    @Query("SELECT h FROM House h WHERE LOWER(REPLACE(h.location, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:location, ' ', ''), '%')) AND h.price <= :maxPrice")
    List<House> searchByLocationAndPrice(@Param("location") String location, @Param("maxPrice") double maxPrice);

    Optional<House> findByLocationAndPriceAndOwner(String location, double price, Owner owner);
}
