package com.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionStatusAnalyticsDto {

    private Long created;

    private Long active;

    private Long ended;
}