package com.auction.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String otp;
    private Long otpExpiry;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Buyer Preferences 
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> preferredCategories;

    private Double minBudget;
    private Double maxBudget;
    private String location;
}