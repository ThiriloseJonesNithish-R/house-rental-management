package com.rentalmanagement.house_rental.repository;

import com.rentalmanagement.house_rental.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByName(String name);

    // ✅ New: resolve current owner from JWT username (email)
    Optional<Owner> findByUser_Email(String email);
}
