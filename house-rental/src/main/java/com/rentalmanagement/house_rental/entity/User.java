package com.rentalmanagement.house_rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // ✅ Explicitly map to existing "users" table
@Getter  
@Setter  
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; 

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; 
}
