package com.auction.Scheduler;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.repository.AuctionRepository;
import com.auction.service.AuctionService;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    // Run every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void updateAuctionStatus() {

        List<Auction> auctions = auctionRepository.findAll();

        for (Auction auction : auctions) {

            LocalDateTime now = LocalDateTime.now();

            // 🔥 START AUCTION
            if (auction.getStatus() == AuctionStatus.CREATED &&
                    now.isAfter(auction.getStartTime())) {

                auction.setStatus(AuctionStatus.ACTIVE);
                auctionRepository.save(auction);

                System.out.println("Auction started: " + auction.getId());
            }

            // 🔥 END AUCTION
            if (auction.getStatus() == AuctionStatus.ACTIVE &&
                    now.isAfter(auction.getEndTime())) {

                auctionService.endAuction(auction.getId());

                System.out.println("Auction ended: " + auction.getId());
            }
        }
    }
}