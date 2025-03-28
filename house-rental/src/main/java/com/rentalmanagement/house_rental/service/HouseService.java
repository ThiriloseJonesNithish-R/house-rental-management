package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.dto.HouseDTO;
import java.util.List;

public interface HouseService {
    HouseDTO addHouse(HouseDTO houseDTO);

    void removeHouse(Long id);

    List<HouseDTO> getAllHouses();

    List<HouseDTO> searchHouses(String location, double maxPrice);

    List<HouseDTO> searchHouses(String location); // ✅ Added overloaded method to allow search without maxPrice

    HouseDTO getHouseById(Long id);
}
