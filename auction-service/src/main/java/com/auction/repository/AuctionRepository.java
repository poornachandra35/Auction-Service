package com.auction.repository;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findByStatus(AuctionStatus status);

    List<Auction> findBySellerId(Long sellerId);

    // ✅ REQUIRED FOR ITEM ↔ AUCTION LINK
    Optional<Auction> findByItemId(Long itemId);
}