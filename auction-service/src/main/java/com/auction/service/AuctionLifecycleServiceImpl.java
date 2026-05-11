package com.auction.service;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;

import com.auction.exception.ResourceNotFoundException;

import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;

import com.auction.service.impl.AuctionLifecycleService;

import com.auction.state.AuctionStateFactory;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionLifecycleServiceImpl
        implements AuctionLifecycleService {

    private final AuctionRepository auctionRepository;

    private final BidRepository bidRepository;

    private final NotificationServiceImpl
            notificationServiceImpl;

    private final AuctionStateFactory
            auctionStateFactory;

    // START AUCTION
    public void startAuction(Long auctionId) {

        log.info(
                "Starting auction with ID: {}",
                auctionId
        );

        Auction auction = auctionRepository
                .findById(auctionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auction not found"
                        ));

        auctionStateFactory
                .getHandler(AuctionStatus.CREATED)
                .validateAuction(auction);

        auction.setStatus(AuctionStatus.ACTIVE);

        auctionRepository.save(auction);

        log.info(
                "Auction started successfully with ID: {}",
                auctionId
        );
    }

    // END AUCTION
    @Transactional
    public void endAuction(Long auctionId) {

        log.info(
                "Ending auction with ID: {}",
                auctionId
        );

        Auction auction = auctionRepository
                .findById(auctionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auction not found"
                        ));

        auctionStateFactory
                .getHandler(AuctionStatus.ACTIVE)
                .validateAuction(auction);

        Bid highestBid = bidRepository
                .findTopByAuctionIdOrderByAmountDesc(
                        auctionId
                )
                .orElse(null);

        // NO BIDS
        if (highestBid == null) {

            log.warn(
                    "Auction ended without bids. Auction ID: {}",
                    auctionId
            );

            auction.setStatus(AuctionStatus.ENDED);

            auctionRepository.save(auction);

            return;
        }

        auction.setWinnerId(
                highestBid.getUserId()
        );

        auction.setCurrentHighestBid(
                highestBid.getAmount()
        );

        auction.setStatus(AuctionStatus.ENDED);

        auctionRepository.save(auction);

        log.info(
                "Auction ended successfully. Winner ID: {} | Final Bid: {}",
                highestBid.getUserId(),
                highestBid.getAmount()
        );

        notificationServiceImpl
                .sendAuctionWinnerNotification(
                        highestBid.getUserId()
                );
    }
}