package com.auction.service.impl;

import com.auction.dto.BidRequest;

import com.auction.entity.Bid;

import java.util.List;

public interface BidService {

    String placeBid(BidRequest request);

    List<Bid> getBidsByAuction(
            Long auctionId
    );

    Double getHighestBid(
            Long auctionId
    );
}