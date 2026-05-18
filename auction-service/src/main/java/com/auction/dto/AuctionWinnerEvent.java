package com.auction.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionWinnerEvent {

    private Long auctionId;

    private Long winnerId;

    private Double winningAmount;

    private String message;
}