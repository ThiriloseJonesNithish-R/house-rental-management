package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.dto.HouseDTO;
import com.rentalmanagement.house_rental.entity.House;
import com.rentalmanagement.house_rental.exception.ResourceNotFoundException;
import com.rentalmanagement.house_rental.repository.HouseRepository;
import com.rentalmanagement.house_rental.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {
    @Autowired
    private HouseRepository houseRepository;

    @Override
    @Transactional
    public HouseDTO addHouse(HouseDTO houseDTO) {
        // Check for duplicate before saving
        Optional<House> existingHouse = houseRepository.findByLocationAndPriceAndOwner(
                houseDTO.getLocation(), houseDTO.getPrice(), houseDTO.getOwner());

        if (existingHouse.isPresent()) {
            throw new IllegalStateException("A house with the same details already exists.");
        }

        House house = new House();
        house.setLocation(houseDTO.getLocation());
        house.setPrice(houseDTO.getPrice());
        house.setBedrooms(houseDTO.getBedrooms());
        house.setOwner(houseDTO.getOwner());

        house = houseRepository.save(house);
        houseDTO.setId(house.getId());
        return houseDTO;
    }

    @Override
    public void removeHouse(Long id) {
        houseRepository.deleteById(id);
    }

    @Override
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ SEARCH METHOD WITHOUT maxPrice (Only by location)
    @Override
    public List<HouseDTO> searchHouses(String location) {
        return houseRepository.findByLocation(location).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ SEARCH METHOD WITH maxPrice
    @Override
    public List<HouseDTO> searchHouses(String location, double maxPrice) {
        return houseRepository.findByLocationAndPriceLessThanEqual(location, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HouseDTO getHouseById(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        return convertToDTO(house);
    }

    private HouseDTO convertToDTO(House house) {
        return new HouseDTO(house.getId(), house.getLocation(), house.getPrice(), house.getBedrooms(),
                house.getOwner());
    }
}
