package com.rentalmanagement.house_rental.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // ✅ This generates the required constructor automatically
@ToString
public class RentalAgreementDTO {
    private Long id;
    private Long houseId;
    private Long tenantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double deposit;
    private List<LocalDate> paymentDueDates;
    private Map<LocalDate, Double> payments; // ✅ Payments stored with date mapping
}
