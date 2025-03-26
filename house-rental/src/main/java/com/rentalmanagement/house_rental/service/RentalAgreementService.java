package com.rentalmanagement.house_rental.service;

import com.rentalmanagement.house_rental.dto.RentalAgreementDTO;
import java.time.LocalDate;
import java.util.List;

public interface RentalAgreementService {
    RentalAgreementDTO createAgreement(RentalAgreementDTO agreementDTO);

    List<RentalAgreementDTO> getAllAgreements();

    List<RentalAgreementDTO> getUpcomingPayments(LocalDate date);

    void recordPayment(Long agreementId, double amount);
}
