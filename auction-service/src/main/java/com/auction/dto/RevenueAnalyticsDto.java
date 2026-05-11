package com.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueAnalyticsDto {

    private Double totalRevenue;

    private Double activeAuctionRevenue;

    private Double endedAuctionRevenue;
}