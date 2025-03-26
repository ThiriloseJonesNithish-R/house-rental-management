package com.rentalmanagement.house_rental.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class RentalAgreementDTO {
    private Long id;
    private Long houseId;
    private Long tenantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double deposit;
    private List<LocalDate> paymentDueDates;
}
