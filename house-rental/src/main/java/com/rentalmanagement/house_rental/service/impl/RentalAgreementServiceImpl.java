package com.rentalmanagement.house_rental.service.impl;

import com.rentalmanagement.house_rental.dto.RentalAgreementDTO;
import com.rentalmanagement.house_rental.entity.House;
import com.rentalmanagement.house_rental.entity.RentalAgreement;
import com.rentalmanagement.house_rental.entity.Tenant;
import com.rentalmanagement.house_rental.exception.ResourceNotFoundException;
import com.rentalmanagement.house_rental.repository.HouseRepository;
import com.rentalmanagement.house_rental.repository.RentalAgreementRepository;
import com.rentalmanagement.house_rental.repository.TenantRepository;
import com.rentalmanagement.house_rental.service.RentalAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalAgreementServiceImpl implements RentalAgreementService {
    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @Override
    @Transactional
    public RentalAgreementDTO createAgreement(RentalAgreementDTO agreementDTO) {
        House house = houseRepository.findById(agreementDTO.getHouseId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));

        Tenant tenant = tenantRepository.findById(agreementDTO.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        Optional<RentalAgreement> existingAgreement = rentalAgreementRepository.findByHouseIdAndTenantId(house.getId(),
                tenant.getId());
        if (existingAgreement.isPresent()) {
            throw new IllegalStateException("Rental agreement already exists for this house and tenant.");
        }

        RentalAgreement agreement = new RentalAgreement();
        agreement.setHouse(house);
        agreement.setTenant(tenant);
        agreement.setStartDate(agreementDTO.getStartDate());
        agreement.setEndDate(agreementDTO.getEndDate());
        agreement.setDeposit(agreementDTO.getDeposit());
        agreement.setPaymentDueDates(agreementDTO.getPaymentDueDates());

        agreement = rentalAgreementRepository.save(agreement);
        agreementDTO.setId(agreement.getId());
        return agreementDTO;
    }

    @Override
    public List<RentalAgreementDTO> getAllAgreements() {
        return rentalAgreementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalAgreementDTO> getUpcomingPayments(LocalDate date) {
        return rentalAgreementRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void recordPayment(Long agreementId, LocalDate date, double amount) {
        RentalAgreement agreement = rentalAgreementRepository.findById(agreementId)
                .orElseThrow(() -> new ResourceNotFoundException("Agreement not found"));

        agreement.recordPayment(date, amount);
        rentalAgreementRepository.save(agreement);
    }

    private RentalAgreementDTO convertToDTO(RentalAgreement agreement) {
        return new RentalAgreementDTO(
                agreement.getId(),
                agreement.getHouse().getId(),
                agreement.getTenant().getId(),
                agreement.getStartDate(),
                agreement.getEndDate(),
                agreement.getDeposit(),
                agreement.getPaymentDueDates(),
                agreement.getPayments() // ✅ Fix: Include payments
        );
    }
}
