package com.auction.itemservice.service;

import com.auction.itemservice.entity.Item;

public interface AuctionIntegrationService {

    void createAuction(Item item);

    void updateAuction(Item item);

    void deleteAuction(Long itemId);
}