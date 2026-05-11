package com.auction.service.impl;

public interface AuctionLifecycleService {

    void startAuction(Long auctionId);

    void endAuction(Long auctionId);
}