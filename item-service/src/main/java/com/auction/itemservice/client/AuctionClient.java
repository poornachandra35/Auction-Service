package com.auction.itemservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.auction.itemservice.dto.AuctionRequest;

@FeignClient(name = "auction-service", url = "http://localhost:8084")
public interface AuctionClient {

    // CREATE
    @PostMapping("/api/auctions")
    void createAuction(@RequestBody AuctionRequest request);

    // ✅ UPDATE (NEW)
    @PutMapping("/api/auctions/by-item/{itemId}")
    void updateAuctionByItemId(
            @PathVariable Long itemId,
            @RequestBody AuctionRequest request
    );

    // ✅ DELETE (NEW)
    @DeleteMapping("/api/auctions/by-item/{itemId}")
    void deleteByItemId(@PathVariable Long itemId);
}