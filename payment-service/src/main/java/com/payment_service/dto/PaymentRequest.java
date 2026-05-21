package com.payment_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(
            message = "Auction Id is required"
    )
    private Long auctionId;

    @NotNull(
            message = "Winner Id is required"
    )
    private Long winnerId;

    @NotNull(
            message = "Amount is required"
    )
    @Min(
            value = 1,
            message = "Amount must be greater than 0"
    )
    private Double amount;
}