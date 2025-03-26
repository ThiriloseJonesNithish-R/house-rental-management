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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private TenantRepository tenantRepository;

    @Override
    @Transactional
    public TenantDTO addTenant(TenantDTO tenantDTO) {
        Optional<Tenant> existingTenant = tenantRepository.findByNameAndContact(
                tenantDTO.getName(), tenantDTO.getContact());

        if (existingTenant.isPresent()) {
            throw new IllegalStateException("A tenant with the same details already exists.");
        }

        Tenant tenant = new Tenant(
                null, // ID will be auto-generated
                tenantDTO.getName(),
                tenantDTO.getContact(),
                tenantDTO.getPreferredLocation());

        tenant = tenantRepository.save(tenant);
        return new TenantDTO(tenant.getId(), tenant.getName(), tenant.getContact(), tenant.getPreferredLocation());
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TenantDTO getTenantById(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        return convertToDTO(tenant);
    }

    private TenantDTO convertToDTO(Tenant tenant) {
        return new TenantDTO(tenant.getId(), tenant.getName(), tenant.getContact(), tenant.getPreferredLocation());
    }
}
