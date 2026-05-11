package com.auction.state;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.exception.BadRequestException;

import org.springframework.stereotype.Component;

@Component
public class CreatedAuctionStateHandler
        implements AuctionStateHandler {

    @Override
    public void validateAuction(Auction auction) {

        if (auction.getStatus() != AuctionStatus.CREATED) {

            throw new BadRequestException(
                    "Auction must be in CREATED state"
            );
        }
    }
}