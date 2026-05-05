package com.auction.repository;

import com.auction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByAuctionIdOrderByAmountDesc(Long auctionId);
    List<Bid> findByAuctionId(Long auctionId);
}