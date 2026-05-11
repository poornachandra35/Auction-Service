package com.auction.state;

import com.auction.entity.Auction;

public interface AuctionStateHandler {

    void validateAuction(Auction auction);
}