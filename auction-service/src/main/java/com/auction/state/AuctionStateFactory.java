package com.auction.state;

import com.auction.entity.AuctionStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionStateFactory {

    private final CreatedAuctionStateHandler createdHandler;

    private final ActiveAuctionStateHandler activeHandler;

    private final EndedAuctionStateHandler endedHandler;

    public AuctionStateHandler getHandler(
            AuctionStatus status
    ) {

        switch (status) {

            case CREATED:
                return createdHandler;

            case ACTIVE:
                return activeHandler;

            case ENDED:
                return endedHandler;

            default:
                throw new IllegalArgumentException(
                        "Invalid auction status"
                );
        }
    }
}