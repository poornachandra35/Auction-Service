package com.auction.service;

import com.auction.dto.BidRequest;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;

import com.auction.exception.BadRequestException;
import com.auction.exception.ResourceNotFoundException;

import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;

import com.auction.service.impl.BidService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidServiceImpl
        implements BidService {

    private final AuctionRepository auctionRepository;

    private final BidRepository bidRepository;

    // PLACE BID
    public String placeBid(BidRequest request) {

        log.info(
                "Bid placement started for auctionId: {}",
                request.getAuctionId()
        );

        Auction auction = auctionRepository
                .findById(request.getAuctionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auction not found"
                        ));

        if (auction.getStatus()
                != AuctionStatus.ACTIVE) {

            log.warn(
                    "Bid rejected because auction is not active. Auction ID: {}",
                    request.getAuctionId()
            );

            throw new BadRequestException(
                    "Auction is not active"
            );
        }

        if (request.getAmount()
                <= auction.getCurrentHighestBid()) {

            log.warn(
                    "Invalid bid amount received for auctionId: {}",
                    request.getAuctionId()
            );

            throw new BadRequestException(
                    "Bid must be higher than current highest bid"
            );
        }

        Bid bid = new Bid();

        bid.setAuctionId(
                request.getAuctionId()
        );

        bid.setUserId(
                request.getUserId()
        );

        bid.setAmount(
                request.getAmount()
        );

        bid.setTimestamp(
                LocalDateTime.now()
        );

        bidRepository.save(bid);

        auction.setCurrentHighestBid(
                request.getAmount()
        );

        auction.setHighestBidderId(
                request.getUserId()
        );

        auctionRepository.save(auction);

        log.info(
                "Bid placed successfully by userId: {} for auctionId: {}",
                request.getUserId(),
                request.getAuctionId()
        );

        return "Bid placed successfully";
    }

    // GET BIDS
    public List<Bid> getBidsByAuction(
            Long auctionId
    ) {

        log.info(
                "Fetching bids for auctionId: {}",
                auctionId
        );

        return bidRepository
                .findByAuctionId(auctionId);
    }

    // GET HIGHEST BID
    public Double getHighestBid(
            Long auctionId
    ) {

        log.info(
                "Fetching highest bid for auctionId: {}",
                auctionId
        );

        Auction auction = auctionRepository
                .findById(auctionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auction not found"
                        ));

        return auction.getCurrentHighestBid();
    }
}