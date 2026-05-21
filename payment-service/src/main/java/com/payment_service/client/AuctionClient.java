package com.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(
        name = "auction-service"
)
public interface AuctionClient {

    @PutMapping(
            "/api/auctions/payment-success/{auctionId}"
    )
    void paymentSuccess(
            @PathVariable Long auctionId
    );
}