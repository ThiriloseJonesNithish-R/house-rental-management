package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.dto.TenantDTO;
import java.util.List;

public interface TenantService {
    TenantDTO addTenant(Long userId, TenantDTO tenantDTO); // ✅ Now includes userId

    List<TenantDTO> getAllTenants();

    TenantDTO getTenantById(Long id);

    TenantDTO getTenantByUserId(Long userId); // ✅ Ensure this method exists
}
