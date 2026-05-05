package com.auction.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BidRequest {

    @NotNull(message = "Auction ID is required")
    private Long auctionId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @Positive(message = "Bid amount must be greater than 0")
    private double amount;
}