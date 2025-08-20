package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.entity.Owner;
import com.rentalmanagement.house_rental.exception.ResourceNotFoundException;
import com.rentalmanagement.house_rental.repository.OwnerRepository;
import com.rentalmanagement.house_rental.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public Owner getOwnerByUserId(Long userId) {
        return ownerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found for user ID: " + userId));
    }
}
