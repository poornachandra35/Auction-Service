package com.auction.service.impl;


import com.auction.dto.AuctionRequest;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;

import java.util.List;

public interface AuctionService {

    Auction createAuction(AuctionRequest request);

    Auction updateAuction(
            Long id,
            AuctionRequest request
    );

    void deleteAuction(Long id);

    List<Auction> getAuctions(
            AuctionStatus status
    );

    Auction updateAuctionByItemId(
            Long itemId,
            AuctionRequest request
    );

    void deleteByItemId(Long itemId);

    Auction getAuctionById(Long id);

    List<Auction> getAuctionsBySeller(
            Long sellerId
    );
}