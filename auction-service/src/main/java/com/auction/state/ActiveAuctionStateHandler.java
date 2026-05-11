package com.auction.state;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.exception.BadRequestException;

import org.springframework.stereotype.Component;

@Component
public class ActiveAuctionStateHandler
        implements AuctionStateHandler {

    @Override
    public void validateAuction(Auction auction) {

        if (auction.getStatus() != AuctionStatus.ACTIVE) {

            throw new BadRequestException(
                    "Auction is not active"
            );
        }
    }
}