package com.auction.itemservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;
    private String imageUrl; // stores file path
    private String category;

    private Double basePrice;

    private LocalDateTime auctionStartTime;

    private LocalDateTime auctionEndTime;

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;
}