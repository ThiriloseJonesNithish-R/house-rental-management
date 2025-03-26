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
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {
    @Autowired
    private HouseRepository houseRepository;

    @Override
    @Transactional
    public HouseDTO addHouse(HouseDTO houseDTO) {
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
    public void removeHouse(Long id) { // Ensure the parameter type is Long
        houseRepository.deleteById(id);
    }

    @Override
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HouseDTO> searchHouses(String location, double maxPrice) {
        return houseRepository.findByLocationAndPriceLessThanEqual(location, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HouseDTO getHouseById(Long id) { // Ensure the parameter type is Long
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        return convertToDTO(house);
    }

    private HouseDTO convertToDTO(House house) {
        HouseDTO dto = new HouseDTO();
        dto.setId(house.getId());
        dto.setLocation(house.getLocation());
        dto.setPrice(house.getPrice());
        dto.setBedrooms(house.getBedrooms());
        dto.setOwner(house.getOwner());
        return dto;
    }
}
