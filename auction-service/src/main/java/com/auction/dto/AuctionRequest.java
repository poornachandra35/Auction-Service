package com.auction.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuctionRequest {

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @Positive(message = "Base price must be greater than 0")
    private double basePrice;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
}