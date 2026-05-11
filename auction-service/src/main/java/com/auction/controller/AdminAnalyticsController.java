package com.auction.controller;

import com.auction.dto.AuctionStatusAnalyticsDto;
import com.auction.dto.BidTrendDto;
import com.auction.dto.DashboardSummaryDto;
import com.auction.dto.HighestAuctionDto;
import com.auction.dto.LiveAuctionDto;
import com.auction.dto.RevenueAnalyticsDto;
import com.auction.dto.TopBuyerDto;
import com.auction.dto.TopSellerDto;

import com.auction.service.impl.AnalyticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auction-admin")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    // DASHBOARD SUMMARY
    @GetMapping("/dashboard/summary")
    public DashboardSummaryDto getDashboardSummary() {

        log.info(
                "Fetching dashboard summary"
        );

        return analyticsService
                .getDashboardSummary();
    }

    // AUCTION STATUS ANALYTICS
    @GetMapping("/analytics/auction-status")
    public AuctionStatusAnalyticsDto
    getAuctionStatusAnalytics() {

        log.info(
                "Fetching auction status analytics"
        );

        return analyticsService
                .getAuctionStatusAnalytics();
    }

    // HIGHEST AUCTIONS
    @GetMapping("/analytics/highest-auctions")
    public List<HighestAuctionDto>
    getHighestAuctions() {

        log.info(
                "Fetching highest auctions"
        );

        return analyticsService
                .getHighestAuctions();
    }

    // TOP SELLERS
    @GetMapping("/analytics/top-sellers")
    public List<TopSellerDto>
    getTopSellers() {

        log.info(
                "Fetching top sellers"
        );

        return analyticsService
                .getTopSellers();
    }

    // TOP BUYERS
    @GetMapping("/analytics/top-buyers")
    public List<TopBuyerDto>
    getTopBuyers() {

        log.info(
                "Fetching top buyers"
        );

        return analyticsService
                .getTopBuyers();
    }

    // BID TRENDS
    @GetMapping("/analytics/bid-trends")
    public List<BidTrendDto>
    getBidTrends() {

        log.info(
                "Fetching bid trends"
        );

        return analyticsService
                .getBidTrends();
    }

    // REVENUE ANALYTICS
    @GetMapping("/analytics/revenue")
    public RevenueAnalyticsDto
    getRevenueAnalytics() {

        log.info(
                "Fetching revenue analytics"
        );

        return analyticsService
                .getRevenueAnalytics();
    }

    // LIVE AUCTIONS
    @GetMapping("/analytics/live-auctions")
    public List<LiveAuctionDto>
    getLiveAuctions() {

        log.info(
                "Fetching live auctions"
        );

        return analyticsService
                .getLiveAuctions();
    }
}