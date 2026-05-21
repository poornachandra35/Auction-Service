package com.auction.service;

import com.auction.entity.Auction;

import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;
import com.auction.dto.AuctionWinnerEvent;

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

    private final AuctionEventProducer
            auctionEventProducer;

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

        auction.setStatus(
                AuctionStatus.ACTIVE
        );

        Auction updatedAuction =
                auctionRepository.save(auction);

        log.info(
                "Auction started successfully with ID: {} and status: {}",
                updatedAuction.getId(),
                updatedAuction.getStatus()
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

        // NO BIDS FOUND
        if (highestBid == null) {

            log.warn(
                    "Auction ended without bids. Auction ID: {}",
                    auctionId
            );

            auction.setStatus(
                    AuctionStatus.ENDED
            );

            Auction endedAuction =
                    auctionRepository.save(auction);

            log.info(
                    "Auction status updated to ENDED for auctionId: {}",
                    endedAuction.getId()
            );

            return;
        }

        // UPDATE HIGHEST BIDDER
        auction.setHighestBidderId(
                highestBid.getUserId()
        );

        // UPDATE WINNER
        auction.setWinnerId(
                highestBid.getUserId()
        );

        // UPDATE FINAL BID
        auction.setCurrentHighestBid(
                highestBid.getAmount()
        );

        // UPDATE STATUS
        auction.setStatus(
                AuctionStatus.ENDED
        );

        Auction endedAuction =
                auctionRepository.save(auction);

        log.info(
                "Auction ended successfully. Auction ID: {} | Winner ID: {} | Final Bid: {}",
                endedAuction.getId(),
                highestBid.getUserId(),
                highestBid.getAmount()
        );

        // CREATE WINNER EVENT
        AuctionWinnerEvent event =
                AuctionWinnerEvent.builder()
                        .auctionId(
                                endedAuction.getId()
                        )
                        .winnerId(
                                highestBid.getUserId()
                        )
                        .winningAmount(
                                highestBid.getAmount()
                        )
                        .message(
                                "Congratulations! You won the auction. Please complete payment."
                        )
                        .build();

        log.info(
                "Publishing auction winner event for auctionId: {} and winnerId: {}",
                endedAuction.getId(),
                highestBid.getUserId()
        );

        auctionEventProducer
                .publishWinnerEvent(event);

        log.info(
                "Auction winner event published successfully for auctionId: {}",
                endedAuction.getId()
        );
    }
}