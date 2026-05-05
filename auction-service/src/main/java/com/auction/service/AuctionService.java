package com.auction.service;

import com.auction.client.NotificationClient;
import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.dto.NotificationRequest;
import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.auction.exception.*;
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final NotificationClient notificationClient;

    // CREATE AUCTION
    public Auction createAuction(AuctionRequest request) {

        Auction auction = new Auction();

        auction.setItemId(request.getItemId());
        auction.setSellerId(request.getSellerId());
        auction.setBasePrice(request.getBasePrice());

        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());

        auction.setStatus(AuctionStatus.CREATED);
        auction.setCurrentHighestBid(request.getBasePrice());

        return auctionRepository.save(auction);
    }

    
    public Auction updateAuction(Long id, AuctionRequest request) {

        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        // ❗ Only allow update if auction not started
        if (auction.getStatus() != AuctionStatus.CREATED) {
            throw new BadRequestException("Only auctions in CREATED state can be updated");
        }

        auction.setItemId(request.getItemId());
        auction.setSellerId(request.getSellerId());
        auction.setBasePrice(request.getBasePrice());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());

        return auctionRepository.save(auction);
    }
    
    
    public void deleteAuction(Long id) {

        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        // ❗ Prevent deleting active auctions
        if (auction.getStatus() == AuctionStatus.ACTIVE) {
            throw new BadRequestException("Cannot delete active auction");
        }

        auctionRepository.delete(auction);
    }
    // 🔥 GET ALL OR FILTERED
    
    public List<Auction> getAuctions(AuctionStatus status) {

        if (status == null) {
            return auctionRepository.findAll();
        }

        return auctionRepository.findByStatus(status);
    }
 // ✅ UPDATE BY ITEM ID
    public Auction updateAuctionByItemId(Long itemId, AuctionRequest request) {

        Auction auction = auctionRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found for item"));

        if (auction.getStatus() != AuctionStatus.CREATED) {
            throw new BadRequestException("Only auctions in CREATED state can be updated");
        }

        auction.setBasePrice(request.getBasePrice());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());

        return auctionRepository.save(auction);
    }

    // ✅ DELETE BY ITEM ID
    public void deleteByItemId(Long itemId) {

        Auction auction = auctionRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found for item"));

        if (auction.getStatus() == AuctionStatus.ACTIVE) {
            throw new BadRequestException("Cannot delete active auction");
        }

        auctionRepository.delete(auction);
    }
    // 🔥 GET BY ID
    

    public Auction getAuctionById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + id));
    }
    // START AUCTION
    public void startAuction(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (auction.getStatus() != AuctionStatus.CREATED) {
            throw new BadRequestException("Auction already started or ended");
        }

        auction.setStatus(AuctionStatus.ACTIVE);
        auctionRepository.save(auction);
    }

    // PLACE BID
    public String placeBid(BidRequest request) {

        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new BadRequestException("Auction is not active");
        }

        if (request.getAmount() <= auction.getCurrentHighestBid()) {
            throw new BadRequestException("Bid must be higher than current highest bid");
        }

        Bid bid = new Bid();
        bid.setAuctionId(request.getAuctionId());
        bid.setUserId(request.getUserId());
        bid.setAmount(request.getAmount());
        bid.setTimestamp(LocalDateTime.now());

        bidRepository.save(bid);

        auction.setCurrentHighestBid(request.getAmount());
        auction.setHighestBidderId(request.getUserId());

        auctionRepository.save(auction);

        return "Bid placed successfully";
    }
    public List<Bid> getBidsByAuction(Long auctionId) {
        return bidRepository.findByAuctionId(auctionId);
    }
    public Double getHighestBid(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        return auction.getCurrentHighestBid();
    }
    
    public List<Auction> getAuctionsBySeller(Long sellerId) {
        return auctionRepository.findBySellerId(sellerId);
    }
    // END AUCTION
    public void endAuction(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new BadRequestException("Auction is not active");
        }

        auction.setStatus(AuctionStatus.ENDED);
        auctionRepository.save(auction);

        if (auction.getHighestBidderId() != null) {

            NotificationRequest request = new NotificationRequest();
            request.setUserId(auction.getHighestBidderId());
            request.setMessage("You won the auction! Complete payment.");

            notificationClient.sendNotification(request);
        }
    }
}