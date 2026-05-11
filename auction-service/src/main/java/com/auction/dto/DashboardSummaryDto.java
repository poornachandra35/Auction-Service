package com.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDto {

    private Long totalAuctions;

    private Long activeAuctions;

    private Long endedAuctions;

    private Long createdAuctions;

    private Long totalBids;
}