package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.dto.TenantDTO;
import java.util.List;

public interface TenantService {
    TenantDTO addTenant(TenantDTO tenantDTO);

    List<TenantDTO> getAllTenants();

    TenantDTO getTenantById(Long id);
}
