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

    @ManyToOne
    private House house;

    @ManyToOne
    private Tenant tenant;

    private LocalDate startDate;
    private LocalDate endDate;
    private double deposit;

    @ElementCollection
    private List<LocalDate> paymentDueDates;

    @ElementCollection
    @CollectionTable(name = "rental_agreement_payments", joinColumns = @JoinColumn(name = "rental_agreement_id"))
    @MapKeyColumn(name = "payment_date") // ✅ Define the key column (date)
    @Column(name = "amount") // ✅ Define the value column (amount)
    private Map<LocalDate, Double> payments = new HashMap<>();

    public void recordPayment(LocalDate date, double amount) {
        // ✅ Step 1: Ensure the date has a due payment
        if (!paymentDueDates.contains(date)) {
            throw new IllegalArgumentException("No payment due on this date: " + date);
        }

        // ✅ Step 2: Get the expected amount for this due date
        double expectedAmount = deposit / paymentDueDates.size();

        // ✅ Step 3: Ensure exact payment amount is paid
        if (amount != expectedAmount) {
            throw new IllegalArgumentException("Incorrect payment amount. Expected: " + expectedAmount);
        }

        // ✅ Step 4: Check if the payment already exists
        if (payments.containsKey(date)) {
            throw new IllegalArgumentException("Payment already recorded for this date: " + date);
        }

        // ✅ Step 5: Record payment
        payments.put(date, amount);
    }

}
