package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.dto.TenantDTO;
import com.rentalmanagement.house_rental.entity.Tenant;
import com.rentalmanagement.house_rental.exception.ResourceNotFoundException;
import com.rentalmanagement.house_rental.repository.TenantRepository;
import com.rentalmanagement.house_rental.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private TenantRepository tenantRepository;

    @Override
    @Transactional
    public TenantDTO addTenant(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        tenant.setName(tenantDTO.getName());
        tenant.setContact(tenantDTO.getContact());
        tenant.setPreferredLocation(tenantDTO.getPreferredLocation());

        tenant = tenantRepository.save(tenant);
        tenantDTO.setId(tenant.getId());
        return tenantDTO;
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TenantDTO getTenantById(Long id) { // Ensure the parameter type is Long
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return convertToDTO(tenant);
    }

    private TenantDTO convertToDTO(Tenant tenant) {
        TenantDTO dto = new TenantDTO();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        dto.setContact(tenant.getContact());
        dto.setPreferredLocation(tenant.getPreferredLocation());
        return dto;
    }
}
