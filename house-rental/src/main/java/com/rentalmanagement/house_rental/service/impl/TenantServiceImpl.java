package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.dto.TenantDTO;
import com.rentalmanagement.house_rental.entity.Tenant;
import com.rentalmanagement.house_rental.entity.User;
import com.rentalmanagement.house_rental.exception.ResourceNotFoundException;
import com.rentalmanagement.house_rental.repository.TenantRepository;
import com.rentalmanagement.house_rental.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public TenantDTO addTenant(Long userId, TenantDTO tenantDTO) {
        // ✅ Check if the User exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // ✅ Prevent creating multiple tenants for the same user
        if (tenantRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("This user is already registered as a tenant.");
        }

        // ✅ Create and associate the Tenant with the User
        Tenant tenant = new Tenant();
        tenant.setName(tenantDTO.getName());
        user.setContact(tenantDTO.getContact());
        tenant.setPreferredLocation(tenantDTO.getPreferredLocation());
        tenant.setUser(user); // ✅ Link user to tenant

        tenant = tenantRepository.save(tenant);
        return convertToDTO(tenant);
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
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with ID: " + id));
        return convertToDTO(tenant);
    }

    @Override
    public TenantDTO getTenantByUserId(Long userId) {
        Tenant tenant = tenantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found for User ID: " + userId));
        return convertToDTO(tenant);
    }

    private TenantDTO convertToDTO(Tenant tenant) {
        return new TenantDTO(tenant.getId(), tenant.getName(), tenant.getUser().getContact(), tenant.getPreferredLocation());
    }
}
