package com.auction.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "bid",
        indexes = {

                @Index(
                        name = "idx_bid_auction",
                        columnList = "auctionId"
                ),

                @Index(
                        name = "idx_bid_user",
                        columnList = "userId"
                ),

                @Index(
                        name = "idx_bid_amount",
                        columnList = "amount"
                ),

                @Index(
                        name = "idx_bid_auction_amount",
                        columnList = "auctionId,amount"
                )
        }
)
@Data
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionId;

    private Long userId;

    private Double amount;

    private LocalDateTime timestamp;
}