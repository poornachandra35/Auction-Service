package com.auction.itemservice.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuctionRequest {

    private Long itemId;
    private Long sellerId;
    private double basePrice;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
