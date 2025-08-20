package com.rentalmanagement.house_rental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_agreement")
public class RentalAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ many agreements → one house
    @JoinColumn(name = "house_id", nullable = false) // ✅ FK column
    private House house;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ many agreements → one tenant
    @JoinColumn(name = "tenant_id", nullable = false) 
    private Tenant tenant;

    private LocalDate startDate;
    private LocalDate endDate;
    private double deposit;

    @ElementCollection
    private List<LocalDate> paymentDueDates;

    @ElementCollection
    @CollectionTable(name = "rental_agreement_payments", joinColumns = @JoinColumn(name = "rental_agreement_id"))
    @MapKeyColumn(name = "payment_date") // ✅ key column (date)
    @Column(name = "amount") // ✅ value column (amount)
    private Map<LocalDate, Double> payments = new HashMap<>();

    public void recordPayment(LocalDate date, double amount) {
        if (!paymentDueDates.contains(date)) {
            throw new IllegalArgumentException("No payment due on this date: " + date);
        }

        double expectedAmount = deposit / paymentDueDates.size();

        if (amount != expectedAmount) {
            throw new IllegalArgumentException("Incorrect payment amount. Expected: " + expectedAmount);
        }

        if (payments.containsKey(date)) {
            throw new IllegalArgumentException("Payment already recorded for this date: " + date);
        }

        payments.put(date, amount);
    }
}
