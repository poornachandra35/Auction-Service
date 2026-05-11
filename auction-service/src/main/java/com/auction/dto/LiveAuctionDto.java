package com.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveAuctionDto {

    private Long auctionId;

    private Double currentBid;

    private Long totalBids;

    private String status;

    private LocalDateTime endTime;
}