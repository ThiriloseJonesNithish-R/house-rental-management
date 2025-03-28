package com.rentalmanagement.house_rental.repository;

import com.rentalmanagement.house_rental.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    // 🔹 FIX: Add method to check if a tenant with same name & contact exists
    Optional<Tenant> findByNameAndContact(String name, String contact);
    Optional<Tenant> findByUserId(Long userId);

}
