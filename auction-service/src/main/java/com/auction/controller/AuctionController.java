package com.auction.controller;

import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;

import com.auction.service.impl.AuctionLifecycleService;
import com.auction.service.impl.AuctionService;
import com.auction.service.impl.BidService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    private final BidService bidService;

    private final AuctionLifecycleService
            auctionLifecycleService;

    // CREATE AUCTION
    @PostMapping
    public Auction createAuction(
            @Valid @RequestBody AuctionRequest request
    ) {

        log.info(
                "Create auction request received for sellerId: {}",
                request.getSellerId()
        );

        Auction auction =
                auctionService.createAuction(
                        request
                );

        log.info(
                "Auction created successfully with ID: {}",
                auction.getId()
        );

        return auction;
    }

    // UPDATE AUCTION BY ID
    @PutMapping("/{id}")
    public Auction updateAuction(
            @PathVariable Long id,
            @Valid @RequestBody AuctionRequest request
    ) {

        log.info(
                "Update auction request received for auctionId: {}",
                id
        );

        Auction auction =
                auctionService.updateAuction(
                        id,
                        request
                );

        log.info(
                "Auction updated successfully with ID: {}",
                id
        );

        return auction;
    }

    // UPDATE AUCTION BY ITEM ID
    @PutMapping("/by-item/{itemId}")
    public Auction updateAuctionByItemId(
            @PathVariable Long itemId,
            @Valid @RequestBody AuctionRequest request
    ) {

        log.info(
                "Update auction by itemId request received: {}",
                itemId
        );

        return auctionService
                .updateAuctionByItemId(
                        itemId,
                        request
                );
    }

    // DELETE AUCTION
    @DeleteMapping("/{id}")
    public String deleteAuction(
            @PathVariable Long id
    ) {

        log.info(
                "Delete auction request received for auctionId: {}",
                id
        );

        auctionService.deleteAuction(id);

        log.info(
                "Auction deleted successfully with ID: {}",
                id
        );

        return "Auction deleted successfully";
    }

    // DELETE AUCTION BY ITEM ID
    @DeleteMapping("/by-item/{itemId}")
    public String deleteByItemId(
            @PathVariable Long itemId
    ) {

        log.info(
                "Delete auction by itemId request received: {}",
                itemId
        );

        auctionService.deleteByItemId(itemId);

        log.info(
                "Auction deleted successfully for itemId: {}",
                itemId
        );

        return "Auction deleted by itemId";
    }

    // PLACE BID
    @PostMapping("/bid")
    public String placeBid(
            @Valid @RequestBody BidRequest request
    ) {

        log.info(
                "Bid placement request received for auctionId: {}",
                request.getAuctionId()
        );

        String response =
                bidService.placeBid(request);

        log.info(
                "Bid placed successfully for auctionId: {}",
                request.getAuctionId()
        );

        return response;
    }

    // GET BIDS
    @GetMapping("/{id}/bids")
    public List<Bid> getBids(
            @PathVariable Long id
    ) {

        log.info(
                "Fetching bids for auctionId: {}",
                id
        );

        return bidService.getBidsByAuction(id);
    }

    // GET HIGHEST BID
    @GetMapping("/{id}/highest-bid")
    public Double getHighestBid(
            @PathVariable Long id
    ) {

        log.info(
                "Fetching highest bid for auctionId: {}",
                id
        );

        return bidService.getHighestBid(id);
    }

    // START AUCTION
    @PutMapping("/{id}/start")
    public String startAuction(
            @PathVariable Long id
    ) {

        log.info(
                "Start auction request received for auctionId: {}",
                id
        );

        auctionLifecycleService.startAuction(id);

        log.info(
                "Auction started successfully with ID: {}",
                id
        );

        return "Auction started successfully";
    }

    // END AUCTION
    @PutMapping("/{id}/end")
    public String endAuction(
            @PathVariable Long id
    ) {

        log.info(
                "End auction request received for auctionId: {}",
                id
        );

        auctionLifecycleService.endAuction(id);

        log.info(
                "Auction ended successfully with ID: {}",
                id
        );

        return "Auction ended successfully";
    }

    // GET AUCTIONS
    @GetMapping
    public List<Auction> getAuctions(
            @RequestParam(required = false)
            AuctionStatus status
    ) {

        log.info(
                "Fetching auctions with status: {}",
                status
        );

        return auctionService.getAuctions(status);
    }

    // GET AUCTION BY ID
    @GetMapping("/{id}")
    public Auction getAuctionById(
            @PathVariable Long id
    ) {

        log.info(
                "Fetching auction by ID: {}",
                id
        );

        return auctionService.getAuctionById(id);
    }

    // GET AUCTIONS BY SELLER
    @GetMapping("/seller/{sellerId}")
    public List<Auction> getAuctionsBySeller(
            @PathVariable Long sellerId
    ) {

        log.info(
                "Fetching auctions for sellerId: {}",
                sellerId
        );

        return auctionService
                .getAuctionsBySeller(
                        sellerId
                );
    }
}