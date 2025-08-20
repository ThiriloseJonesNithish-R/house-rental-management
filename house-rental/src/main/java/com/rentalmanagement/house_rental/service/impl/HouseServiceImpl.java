package com.rentalmanagement.house_rental.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentalmanagement.house_rental.dto.HouseDTO;
import com.rentalmanagement.house_rental.dto.MessageResponse;
import com.rentalmanagement.house_rental.entity.House;
import com.rentalmanagement.house_rental.entity.Owner;
import com.rentalmanagement.house_rental.exception.ResourceNotFoundException;
import com.rentalmanagement.house_rental.repository.HouseRepository;
import com.rentalmanagement.house_rental.repository.OwnerRepository;
import com.rentalmanagement.house_rental.service.HouseService;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : null; // this is the email/username in JWT
    }

    private Owner getCurrentOwnerOrThrow() {
        String jwtEmail = getCurrentUsername();
        if (jwtEmail == null) {
            throw new SecurityException("Unauthenticated request.");
        }
        return ownerRepository.findByUser_Email(jwtEmail)
                .orElseThrow(() -> new SecurityException("Only owners can perform this action."));
    }

    @Override
    @Transactional
    public HouseDTO addHouse(HouseDTO houseDTO) {
        // 🔐 Resolve owner strictly from JWT
        Owner currentOwner = getCurrentOwnerOrThrow();

        // (Optional) prevent spoofing via body ownerName
        if (houseDTO.getOwnerName() != null && !houseDTO.getOwnerName().isBlank()
                && !currentOwner.getName().equals(houseDTO.getOwnerName())) {
            throw new SecurityException("Owner mismatch. You can only add houses under your own account.");
        }

        // ✅ Duplicate check (location + price + owner)
        Optional<House> existingHouse = houseRepository.findByLocationAndPriceAndOwner(
                houseDTO.getLocation(), houseDTO.getPrice(), currentOwner);
        if (existingHouse.isPresent()) {
            throw new IllegalStateException("A house with the same details already exists.");
        }

        // ✅ Save
        House house = new House();
        house.setLocation(houseDTO.getLocation());
        house.setPrice(houseDTO.getPrice());
        house.setBedrooms(houseDTO.getBedrooms());
        house.setOwner(currentOwner);

        house = houseRepository.save(house);

        return convertToDTO(house);
    }

    @Override
    @Transactional
    public MessageResponse updateHouse(Long id, HouseDTO houseDTO) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + id));

        // 🔐 Must be this house's owner
        Owner currentOwner = getCurrentOwnerOrThrow();
        if (!house.getOwner().getId().equals(currentOwner.getId())) {
            throw new SecurityException("You are not allowed to update this house.");
        }

        // 🔒 Restrict location change
        if (!house.getLocation().equals(houseDTO.getLocation())) {
            throw new IllegalArgumentException("Location cannot be changed");
        }

        // 🔒 Restrict bedrooms change
        if (house.getBedrooms() != houseDTO.getBedrooms()) {
            throw new IllegalArgumentException("Bedrooms cannot be changed");
        }

        // 🔒 Owner cannot be changed
        if (houseDTO.getOwnerName() != null && !houseDTO.getOwnerName().isBlank()
                && !house.getOwner().getName().equals(houseDTO.getOwnerName())) {
            throw new IllegalArgumentException("Owner cannot be changed");
        }

        // 🔄 Price can change only if no active agreements
        if (house.getPrice() != houseDTO.getPrice()) {
            boolean occupied = !house.getAgreements().isEmpty();
            if (occupied) {
                throw new IllegalArgumentException("Cannot change price while tenant is living there");
            }
            house.setPrice(houseDTO.getPrice());
        }

        houseRepository.save(house);

        return new MessageResponse("House updated successfully! Updated details: " + convertToDTO(house));
    }

    @Override
    @Transactional
    public void removeHouse(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + id));

        // 🔐 Must be this house's owner
        Owner currentOwner = getCurrentOwnerOrThrow();
        if (!house.getOwner().getId().equals(currentOwner.getId())) {
            throw new SecurityException("You are not allowed to delete this house.");
        }

        houseRepository.delete(house);
    }

    @Override
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HouseDTO> searchHouses(String location) {
        List<House> houses = houseRepository.searchByLocation(location);
        if (houses.isEmpty()) {
            throw new ResourceNotFoundException(
                    new MessageResponse("No houses found in '" + location
                            + "'. Please check your spelling or try a nearby location.")
                            .getMessage());
        }
        return houses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<HouseDTO> searchHouses(String location, double maxPrice) {
        List<House> houses = houseRepository.searchByLocationAndPrice(location, maxPrice);
        if (houses.isEmpty()) {
            throw new ResourceNotFoundException(new MessageResponse(
                    "No houses found in '" + location + "' under ₹" + maxPrice
                            + ". Try increasing your budget or checking your spelling.")
                    .getMessage());
        }
        return houses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public HouseDTO getHouseById(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No house found for the given ID: " + id + ". Please check the ID and try again."));
        return convertToDTO(house);
    }

    private HouseDTO convertToDTO(House house) {
        return new HouseDTO(
                house.getId(),
                house.getLocation(),
                house.getPrice(),
                house.getBedrooms(),
                house.getOwner().getName()
        );
    }
}
