package com.rentalmanagement.house_rental.controller;

import com.rentalmanagement.house_rental.dto.RentalAgreementDTO;
import com.rentalmanagement.house_rental.service.RentalAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rental-agreements")
@CrossOrigin
public class RentalAgreementController {
    @Autowired
    private RentalAgreementService rentalAgreementService;

    @PostMapping
    public ResponseEntity<RentalAgreementDTO> createAgreement(@RequestBody RentalAgreementDTO agreementDTO) {
        return ResponseEntity.ok(rentalAgreementService.createAgreement(agreementDTO));
    }

    @GetMapping
    public ResponseEntity<List<RentalAgreementDTO>> getAllAgreements() {
        return ResponseEntity.ok(rentalAgreementService.getAllAgreements());
    }

    @GetMapping("/upcoming-payments")
    public ResponseEntity<List<RentalAgreementDTO>> getUpcomingPayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(rentalAgreementService.getUpcomingPayments(date));
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<Void> recordPayment(
            @PathVariable("id") Long agreementId,
            @RequestParam double amount) {
        rentalAgreementService.recordPayment(agreementId, amount);
        return ResponseEntity.ok().build();
    }
}
