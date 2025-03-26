package com.rentalmanagement.house_rental.controller;

import com.rentalmanagement.house_rental.dto.RentalAgreementDTO;
import com.rentalmanagement.house_rental.service.RentalAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    public ResponseEntity<Map<String, String>> recordPayment(
            @PathVariable("id") Long agreementId,
            @RequestBody Map<String, Object> paymentData) { // ✅ Accepts JSON body

        double amount = Double.parseDouble(paymentData.get("amount").toString());
        LocalDate date = LocalDate.parse(paymentData.get("date").toString());

        rentalAgreementService.recordPayment(agreementId, date, amount);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Payment recorded successfully");

        return ResponseEntity.ok(response);
    }

}
