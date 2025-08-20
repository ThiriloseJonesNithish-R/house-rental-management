package com.rentalmanagement.house_rental.repository;

import com.rentalmanagement.house_rental.entity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {

    // Find rental agreements where payments are due
    List<RentalAgreement> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);

    // 🔹 FIX: Add method to check if agreement exists for a house & tenant
    Optional<RentalAgreement> findByHouseIdAndTenantId(Long houseId, Long tenantId);

    boolean existsByHouseId(Long houseId);
}
