package com.auction.service;

import com.auction.client.UserServiceClient;

import com.auction.dto.AuctionStatusAnalyticsDto;
import com.auction.dto.BidTrendDto;
import com.auction.dto.DashboardSummaryDto;
import com.auction.dto.HighestAuctionDto;
import com.auction.dto.LiveAuctionDto;
import com.auction.dto.RevenueAnalyticsDto;
import com.auction.dto.TopBuyerDto;
import com.auction.dto.TopSellerDto;
import com.auction.dto.UserResponseDto;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;

import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;

import com.auction.service.impl.AnalyticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl
        implements AnalyticsService {

    private final AuctionRepository
            auctionRepository;

    private final BidRepository
            bidRepository;

    private final UserServiceClient
            userServiceClient;

    // DASHBOARD SUMMARY
    public DashboardSummaryDto
    getDashboardSummary() {

        log.info(
                "Fetching dashboard summary analytics"
        );

        Long totalAuctions =
                auctionRepository.count();

        Long activeAuctions =
                auctionRepository.countByStatus(
                        AuctionStatus.ACTIVE
                );

        Long endedAuctions =
                auctionRepository.countByStatus(
                        AuctionStatus.ENDED
                );

        Long createdAuctions =
                auctionRepository.countByStatus(
                        AuctionStatus.CREATED
                );

        Long totalBids =
                bidRepository.count();

        log.info(
                "Dashboard summary fetched successfully"
        );

        return new DashboardSummaryDto(
                totalAuctions,
                activeAuctions,
                endedAuctions,
                createdAuctions,
                totalBids
        );
    }

    // AUCTION STATUS ANALYTICS
    public AuctionStatusAnalyticsDto
    getAuctionStatusAnalytics() {

        log.info(
                "Fetching auction status analytics"
        );

        Long created =
                auctionRepository.countByStatus(
                        AuctionStatus.CREATED
                );

        Long active =
                auctionRepository.countByStatus(
                        AuctionStatus.ACTIVE
                );

        Long ended =
                auctionRepository.countByStatus(
                        AuctionStatus.ENDED
                );

        log.info(
                "Auction status analytics fetched successfully"
        );

        return new AuctionStatusAnalyticsDto(
                created,
                active,
                ended
        );
    }

    // HIGHEST AUCTIONS
    public List<HighestAuctionDto>
    getHighestAuctions() {

        log.info(
                "Fetching highest auctions analytics"
        );

        List<HighestAuctionDto> result =
                auctionRepository
                        .findTop5ByOrderByCurrentHighestBidDesc()
                        .stream()
                        .map(a -> {

                            UserResponseDto seller =
                                    userServiceClient
                                            .getUserById(
                                                    a.getSellerId()
                                            );

                            return new HighestAuctionDto(
                                    a.getId(),
                                    seller.getName(),
                                    a.getStatus(),
                                    a.getCurrentHighestBid()
                            );
                        })
                        .toList();

        log.info(
                "Highest auctions analytics fetched successfully"
        );

        return result;
    }

    // TOP SELLERS
    public List<TopSellerDto>
    getTopSellers() {

        log.info(
                "Fetching top sellers analytics"
        );

        List<TopSellerDto> result =
                auctionRepository
                        .getTopSellers()
                        .stream()
                        .map(obj -> {

                            Long sellerId =
                                    (Long) obj[0];

                            Long totalAuctions =
                                    (Long) obj[1];

                            UserResponseDto seller =
                                    userServiceClient
                                            .getUserById(
                                                    sellerId
                                            );

                            Double revenue =
                                    auctionRepository
                                            .findBySellerId(
                                                    sellerId
                                            )
                                            .stream()
                                            .mapToDouble(
                                                    Auction::getCurrentHighestBid
                                            )
                                            .sum();

                            return new TopSellerDto(
                                    seller.getName(),
                                    totalAuctions,
                                    revenue
                            );
                        })
                        .toList();

        log.info(
                "Top sellers analytics fetched successfully"
        );

        return result;
    }

    // TOP BUYERS
    public List<TopBuyerDto>
    getTopBuyers() {

        log.info(
                "Fetching top buyers analytics"
        );

        List<TopBuyerDto> result =
                bidRepository
                        .getTopBuyers()
                        .stream()
                        .map(obj -> {

                            Long buyerId =
                                    (Long) obj[0];

                            Long totalBids =
                                    (Long) obj[1];

                            UserResponseDto buyer =
                                    userServiceClient
                                            .getUserById(
                                                    buyerId
                                            );

                            Long wonAuctions =
                                    auctionRepository
                                            .findAll()
                                            .stream()
                                            .filter(a ->
                                                    a.getWinnerId() != null &&
                                                    a.getWinnerId()
                                                            .equals(
                                                                    buyerId
                                                            )
                                            )
                                            .count();

                            return new TopBuyerDto(
                                    buyer.getName(),
                                    totalBids,
                                    wonAuctions
                            );
                        })
                        .toList();

        log.info(
                "Top buyers analytics fetched successfully"
        );

        return result;
    }

    // BID TRENDS
    public List<BidTrendDto>
    getBidTrends() {

        log.info(
                "Fetching bid trends analytics"
        );

        List<Object[]> results =
                bidRepository.getBidTrends();

        List<BidTrendDto> response =
                results.stream()
                        .map(obj -> new BidTrendDto(
                                obj[0].toString(),
                                (Long) obj[1]
                        ))
                        .toList();

        log.info(
                "Bid trends analytics fetched successfully"
        );

        return response;
    }

    // REVENUE ANALYTICS
    public RevenueAnalyticsDto
    getRevenueAnalytics() {

        log.info(
                "Fetching revenue analytics"
        );

        Double totalRevenue =
                auctionRepository.getTotalRevenue();

        Double activeRevenue =
                auctionRepository
                        .getActiveAuctionRevenue();

        Double endedRevenue =
                auctionRepository
                        .getEndedAuctionRevenue();

        log.info(
                "Revenue analytics fetched successfully"
        );

        return new RevenueAnalyticsDto(
                totalRevenue,
                activeRevenue,
                endedRevenue
        );
    }

    // LIVE AUCTIONS
    public List<LiveAuctionDto>
    getLiveAuctions() {

        log.info(
                "Fetching live auctions analytics"
        );

        List<LiveAuctionDto> result =
                auctionRepository
                        .findByStatus(
                                AuctionStatus.ACTIVE
                        )
                        .stream()
                        .map(auction -> {

                            Long totalBids =
                                    (long) bidRepository
                                            .findByAuctionId(
                                                    auction.getId()
                                            )
                                            .size();

                            return new LiveAuctionDto(
                                    auction.getId(),

                                    auction.getCurrentHighestBid(),

                                    totalBids,

                                    auction.getStatus().name(),

                                    auction.getEndTime()
                            );
                        })
                        .toList();

        log.info(
                "Live auctions analytics fetched successfully"
        );

        return result;
    }
}