package com.auction.entity;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(
        name = "auction",
        indexes = {

                @Index(
                        name = "idx_auction_status",
                        columnList = "status"
                ),

                @Index(
                        name = "idx_auction_seller",
                        columnList = "sellerId"
                ),

                @Index(
                        name = "idx_auction_item",
                        columnList = "itemId"
                ),

                @Index(
                        name = "idx_auction_start_time",
                        columnList = "startTime"
                ),

                @Index(
                        name = "idx_auction_end_time",
                        columnList = "endTime"
                ),

                @Index(
                        name = "idx_auction_status_start",
                        columnList = "status,startTime"
                ),

                @Index(
                        name = "idx_auction_status_end",
                        columnList = "status,endTime"
                )
        }
)
@Data
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private Long sellerId;

    private Double basePrice;

    private Double currentHighestBid;

    private Long highestBidderId;

    private Long winnerId;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}