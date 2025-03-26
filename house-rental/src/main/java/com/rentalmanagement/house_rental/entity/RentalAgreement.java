package com.rentalmanagement.house_rental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "rental_agreement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private List<LocalDate> paymentDueDates = new ArrayList<>();

    @ElementCollection
    private List<Double> payments = new ArrayList<>();

    public void recordPayment(double amount) {
        if (payments == null) {
            payments = new ArrayList<>();
        }
        payments.add(amount);
    }
}
