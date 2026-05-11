package com.auction.service.impl;

import com.auction.dto.*;

import java.util.List;

public interface AnalyticsService {

    DashboardSummaryDto getDashboardSummary();

    AuctionStatusAnalyticsDto getAuctionStatusAnalytics();

    List<HighestAuctionDto> getHighestAuctions();

    List<TopSellerDto> getTopSellers();

    List<TopBuyerDto> getTopBuyers();

    List<BidTrendDto> getBidTrends();

    RevenueAnalyticsDto getRevenueAnalytics();

    List<LiveAuctionDto> getLiveAuctions();
}