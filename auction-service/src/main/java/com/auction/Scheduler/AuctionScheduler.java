package com.auction.scheduler;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;

import com.auction.repository.AuctionRepository;

import com.auction.service.impl.AuctionLifecycleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;

    private final AuctionLifecycleService
            auctionLifecycleService;

    // RUN EVERY 30 SECONDS
    @Scheduled(fixedRate = 30000)
    public void updateAuctionStatus() {

        log.info(
                "Auction scheduler triggered"
        );

        LocalDateTime now =
                LocalDateTime.now();

        // AUCTIONS TO START
        List<Auction> auctionsToStart =
                auctionRepository
                        .findByStatusAndStartTimeBefore(
                                AuctionStatus.CREATED,
                                now
                        );

        // START AUCTIONS
        for (Auction auction : auctionsToStart) {

            auctionLifecycleService
                    .startAuction(auction.getId());

            log.info(
                    "Auction started with ID: {}",
                    auction.getId()
            );
        }

        // AUCTIONS TO END
        List<Auction> auctionsToEnd =
                auctionRepository
                        .findByStatusAndEndTimeBefore(
                                AuctionStatus.ACTIVE,
                                now
                        );

        // END AUCTIONS
        for (Auction auction : auctionsToEnd) {

            auctionLifecycleService
                    .endAuction(auction.getId());

            log.info(
                    "Auction ended with ID: {}",
                    auction.getId()
            );
        }
    }
}