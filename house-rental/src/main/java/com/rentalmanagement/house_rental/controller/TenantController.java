package com.rentalmanagement.house_rental.controller;

import com.rentalmanagement.house_rental.dto.TenantDTO;
import com.rentalmanagement.house_rental.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    public ResponseEntity<TenantDTO> addTenant(@RequestParam Long userId, @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(tenantService.addTenant(userId, tenantDTO)); // ✅ Pass userId
    }

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable Long id) { 
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<TenantDTO> getTenantByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tenantService.getTenantByUserId(userId));
    }
}
