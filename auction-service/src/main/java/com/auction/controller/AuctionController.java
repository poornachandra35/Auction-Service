package com.auction.controller;

import com.auction.dto.AuctionRequest;
import jakarta.validation.Valid;

import com.auction.dto.BidRequest;
import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;
import com.auction.service.AuctionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    // CREATE
    @PostMapping
    public Auction createAuction(@Valid @RequestBody AuctionRequest request) {
        return auctionService.createAuction(request);
    }

    // EXISTING UPDATE (BY AUCTION ID)
    @PutMapping("/{id}")
    public Auction updateAuction(
            @PathVariable Long id,
            @Valid @RequestBody AuctionRequest request
    ) {
        return auctionService.updateAuction(id, request);
    }

    // ✅ NEW UPDATE BY ITEM ID
    @PutMapping("/by-item/{itemId}")
    public Auction updateAuctionByItemId(
            @PathVariable Long itemId,
            @Valid @RequestBody AuctionRequest request
    ) {
        return auctionService.updateAuctionByItemId(itemId, request);
    }

    // DELETE BY AUCTION ID
    @DeleteMapping("/{id}")
    public String deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return "Auction deleted successfully";
    }

    // ✅ NEW DELETE BY ITEM ID
    @DeleteMapping("/by-item/{itemId}")
    public String deleteByItemId(@PathVariable Long itemId) {
        auctionService.deleteByItemId(itemId);
        return "Auction deleted by itemId";
    }

    @PostMapping("/bid")
    public String placeBid(@Valid @RequestBody BidRequest request) {
        return auctionService.placeBid(request);
    }

    @GetMapping("/seller/{sellerId}")
    public List<Auction> getAuctionsBySeller(@PathVariable Long sellerId) {
        return auctionService.getAuctionsBySeller(sellerId);
    }

    @GetMapping("/{id}/bids")
    public List<Bid> getBids(@PathVariable Long id) {
        return auctionService.getBidsByAuction(id);
    }

    @GetMapping("/{id}/highest-bid")
    public Double getHighestBid(@PathVariable Long id) {
        return auctionService.getHighestBid(id);
    }

    @GetMapping
    public List<Auction> getAuctions(@RequestParam(required = false) AuctionStatus status) {
        return auctionService.getAuctions(status);
    }

    @GetMapping("/{id}")
    public Auction getAuctionById(@PathVariable Long id) {
        return auctionService.getAuctionById(id);
    }
}