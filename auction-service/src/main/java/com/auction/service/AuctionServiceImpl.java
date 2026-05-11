package com.auction.service;

import com.auction.dto.AuctionRequest;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;

import com.auction.exception.BadRequestException;
import com.auction.exception.ResourceNotFoundException;

import com.auction.repository.AuctionRepository;

import com.auction.service.impl.AuctionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl
        implements AuctionService {

    private final AuctionRepository auctionRepository;

    // CREATE AUCTION
    public Auction createAuction(
            AuctionRequest request
    ) {

        log.info(
                "Creating auction for sellerId: {}",
                request.getSellerId()
        );

        Auction auction = new Auction();

        auction.setItemId(request.getItemId());

        auction.setSellerId(
                request.getSellerId()
        );

        auction.setBasePrice(
                request.getBasePrice()
        );

        auction.setStartTime(
                request.getStartTime()
        );

        auction.setEndTime(
                request.getEndTime()
        );

        auction.setStatus(
                AuctionStatus.CREATED
        );

        auction.setCurrentHighestBid(
                request.getBasePrice()
        );

        Auction savedAuction =
                auctionRepository.save(auction);

        log.info(
                "Auction created successfully with ID: {}",
                savedAuction.getId()
        );

        return savedAuction;
    }

    // UPDATE AUCTION
    public Auction updateAuction(
            Long id,
            AuctionRequest request
    ) {

        log.info(
                "Updating auction with ID: {}",
                id
        );

        Auction auction =
                auctionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Auction not found"
                                ));

        if (auction.getStatus()
                != AuctionStatus.CREATED) {

            log.warn(
                    "Auction update rejected for auctionId: {}",
                    id
            );

            throw new BadRequestException(
                    "Only auctions in CREATED state can be updated"
            );
        }

        auction.setItemId(request.getItemId());

        auction.setSellerId(
                request.getSellerId()
        );

        auction.setBasePrice(
                request.getBasePrice()
        );

        auction.setStartTime(
                request.getStartTime()
        );

        auction.setEndTime(
                request.getEndTime()
        );

        Auction updatedAuction =
                auctionRepository.save(auction);

        log.info(
                "Auction updated successfully with ID: {}",
                id
        );

        return updatedAuction;
    }

    // DELETE AUCTION
    public void deleteAuction(Long id) {

        log.info(
                "Deleting auction with ID: {}",
                id
        );

        Auction auction =
                auctionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Auction not found"
                                ));

        if (auction.getStatus()
                == AuctionStatus.ACTIVE) {

            log.warn(
                    "Cannot delete active auction with ID: {}",
                    id
            );

            throw new BadRequestException(
                    "Cannot delete active auction"
            );
        }

        auctionRepository.delete(auction);

        log.info(
                "Auction deleted successfully with ID: {}",
                id
        );
    }

    // GET ALL AUCTIONS
    public List<Auction> getAuctions(
            AuctionStatus status
    ) {

        log.info(
                "Fetching auctions with status: {}",
                status
        );

        if (status == null) {

            return auctionRepository.findAll();
        }

        return auctionRepository
                .findByStatus(status);
    }

    // UPDATE BY ITEM ID
    public Auction updateAuctionByItemId(
            Long itemId,
            AuctionRequest request
    ) {

        log.info(
                "Updating auction using itemId: {}",
                itemId
        );

        Auction auction =
                auctionRepository.findByItemId(itemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Auction not found for item"
                                ));

        if (auction.getStatus()
                != AuctionStatus.CREATED) {

            log.warn(
                    "Auction update rejected for itemId: {}",
                    itemId
            );

            throw new BadRequestException(
                    "Only auctions in CREATED state can be updated"
            );
        }

        auction.setBasePrice(
                request.getBasePrice()
        );

        auction.setStartTime(
                request.getStartTime()
        );

        auction.setEndTime(
                request.getEndTime()
        );

        Auction updatedAuction =
                auctionRepository.save(auction);

        log.info(
                "Auction updated successfully using itemId: {}",
                itemId
        );

        return updatedAuction;
    }

    // DELETE BY ITEM ID
    public void deleteByItemId(Long itemId) {

        log.info(
                "Deleting auction using itemId: {}",
                itemId
        );

        Auction auction =
                auctionRepository.findByItemId(itemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Auction not found for item"
                                ));

        if (auction.getStatus()
                == AuctionStatus.ACTIVE) {

            log.warn(
                    "Cannot delete active auction using itemId: {}",
                    itemId
            );

            throw new BadRequestException(
                    "Cannot delete active auction"
            );
        }

        auctionRepository.delete(auction);

        log.info(
                "Auction deleted successfully using itemId: {}",
                itemId
        );
    }

    // GET BY ID
    public Auction getAuctionById(Long id) {

        log.info(
                "Fetching auction by ID: {}",
                id
        );

        return auctionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auction not found with id: " + id
                        ));
    }

    // GET AUCTIONS BY SELLER
    public List<Auction> getAuctionsBySeller(
            Long sellerId
    ) {

        log.info(
                "Fetching auctions for sellerId: {}",
                sellerId
        );

        return auctionRepository
                .findBySellerId(sellerId);
    }
}