package com.rentalmanagement.house_rental.controller;

import com.rentalmanagement.house_rental.dto.HouseDTO;
import com.rentalmanagement.house_rental.dto.MessageResponse;
import com.rentalmanagement.house_rental.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin(origins = "*")
public class HouseController {
    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping
    public ResponseEntity<HouseDTO> addHouse(@RequestBody HouseDTO houseDTO) {
        return ResponseEntity.ok(houseService.addHouse(houseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateHouse(@PathVariable Long id, @RequestBody HouseDTO houseDTO) {
        MessageResponse response = houseService.updateHouse(id, houseDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeHouse(@PathVariable Long id) {
        houseService.removeHouse(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<HouseDTO>> getAllHouses() {
        return ResponseEntity.ok(houseService.getAllHouses());
    }

    @GetMapping("/search")
    public ResponseEntity<List<HouseDTO>> searchHouses(
            @RequestParam String location,
            @RequestParam(required = false) Double maxPrice) {
        List<HouseDTO> houses;
        if (maxPrice != null) {
            houses = houseService.searchHouses(location, maxPrice);
        } else {
            houses = houseService.searchHouses(location);
        }
        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseDTO> getHouseById(@PathVariable Long id) {
        return ResponseEntity.ok(houseService.getHouseById(id));
    }
}
