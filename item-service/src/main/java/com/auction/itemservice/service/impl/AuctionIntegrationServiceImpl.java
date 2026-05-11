package com.auction.itemservice.service.impl;

import com.auction.itemservice.client.AuctionClient;
import com.auction.itemservice.dto.AuctionRequest;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.service.AuctionIntegrationService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionIntegrationServiceImpl
        implements AuctionIntegrationService {

    private final AuctionClient auctionClient;

    // =====================================================
    // CREATE AUCTION
    // =====================================================

    @Override
    @CircuitBreaker(
            name = "auctionService",
            fallbackMethod = "createAuctionFallback"
    )
    @Retry(name = "auctionService")
    public void createAuction(Item item) {

        log.info(
                "Calling auction-service for item: {}",
                item.getId()
        );

        AuctionRequest request =
                buildRequest(item);

        auctionClient.createAuction(request);

        log.info(
                "Auction created successfully for item: {}",
                item.getId()
        );
    }

    // =====================================================
    // UPDATE AUCTION
    // =====================================================

    @Override
    @CircuitBreaker(
            name = "auctionService",
            fallbackMethod = "updateAuctionFallback"
    )
    @Retry(name = "auctionService")
    public void updateAuction(Item item) {

        log.info(
                "Updating auction for item: {}",
                item.getId()
        );

        AuctionRequest request =
                buildRequest(item);

        auctionClient.updateAuctionByItemId(
                item.getId(),
                request
        );

        log.info(
                "Auction updated successfully"
        );
    }

    // =====================================================
    // DELETE AUCTION
    // =====================================================

    @Override
    @CircuitBreaker(
            name = "auctionService",
            fallbackMethod = "deleteAuctionFallback"
    )
    @Retry(name = "auctionService")
    public void deleteAuction(Long itemId) {

        log.info(
                "Deleting auction for item: {}",
                itemId
        );

        auctionClient.deleteByItemId(itemId);

        log.info(
                "Auction deleted successfully"
        );
    }

    // =====================================================
    // COMMON REQUEST BUILDER
    // =====================================================

    private AuctionRequest buildRequest(
            Item item
    ) {

        AuctionRequest request =
                new AuctionRequest();

        request.setItemId(item.getId());

        request.setSellerId(item.getSellerId());

        request.setBasePrice(item.getBasePrice());

        request.setStartTime(
                item.getAuctionStartTime()
        );

        request.setEndTime(
                item.getAuctionEndTime()
        );

        return request;
    }

    // =====================================================
    // FALLBACK METHODS
    // =====================================================

    public void createAuctionFallback(
            Item item,
            Exception ex
    ) {

        log.error(
                "Fallback executed: createAuction failed for item {}",
                item.getId(),
                ex
        );
    }

    public void updateAuctionFallback(
            Item item,
            Exception ex
    ) {

        log.error(
                "Fallback executed: updateAuction failed for item {}",
                item.getId(),
                ex
        );
    }

    public void deleteAuctionFallback(
            Long itemId,
            Exception ex
    ) {

        log.error(
                "Fallback executed: deleteAuction failed for item {}",
                itemId,
                ex
        );
    }
}