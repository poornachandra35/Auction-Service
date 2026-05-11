package com.auction.state;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.exception.BadRequestException;

import org.springframework.stereotype.Component;

@Component
public class EndedAuctionStateHandler
        implements AuctionStateHandler {

    @Override
    public void validateAuction(Auction auction) {

        if (auction.getStatus() != AuctionStatus.ENDED) {

            throw new BadRequestException(
                    "Auction must be ended"
            );
        }
    }
}