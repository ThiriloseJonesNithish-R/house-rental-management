package com.rentalmanagement.house_rental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private double price;
    private int bedrooms;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // Foreign key linking to Owner
    private Owner owner;

    // ✅ Bi-directional link to RentalAgreement
    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RentalAgreement> agreements = new ArrayList<>();
}
