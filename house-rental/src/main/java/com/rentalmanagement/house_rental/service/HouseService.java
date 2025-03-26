package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.dto.HouseDTO;
import java.util.List;

public interface HouseService {
    HouseDTO addHouse(HouseDTO houseDTO);

    void removeHouse(Long id); // Make sure this matches the implementation

    List<HouseDTO> getAllHouses();

    List<HouseDTO> searchHouses(String location, double maxPrice);

    HouseDTO getHouseById(Long id); // Make sure this matches the implementation
}
