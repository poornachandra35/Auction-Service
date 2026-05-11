package com.auction.dto;

import com.auction.entity.AuctionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighestAuctionDto {

    private Long auctionId;

    private String sellerName;

    private AuctionStatus status;

    private Double highestBid;
}