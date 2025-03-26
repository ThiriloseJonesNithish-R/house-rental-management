package com.rentalmanagement.house_rental.repository;

import com.rentalmanagement.house_rental.entity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
    List<RentalAgreement> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);
}
