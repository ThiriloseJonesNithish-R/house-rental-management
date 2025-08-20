package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.entity.Owner;

public interface OwnerService {
    Owner getOwnerByUserId(Long userId);
}
