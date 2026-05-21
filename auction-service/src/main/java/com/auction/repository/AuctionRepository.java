package com.auction.repository;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository
        extends JpaRepository<Auction, Long> {
	List<Auction> findByWinnerId(Long winnerId);

    List<Auction> findByStatus(
            AuctionStatus status
    );

    List<Auction> findBySellerId(
            Long sellerId
    );

    long countByStatus(
            AuctionStatus status
    );

    // ITEM ↔ AUCTION LINK
    Optional<Auction> findByItemId(
            Long itemId
    );

    // START SCHEDULER QUERY
    List<Auction> findByStatusAndStartTimeBefore(
            AuctionStatus status,
            LocalDateTime time
    );

    // END SCHEDULER QUERY
    List<Auction> findByStatusAndEndTimeBefore(
            AuctionStatus status,
            LocalDateTime time
    );

    // TOP SELLERS
    @Query("""
           SELECT a.sellerId, COUNT(a)
           FROM Auction a
           GROUP BY a.sellerId
           ORDER BY COUNT(a) DESC
           """)
    List<Object[]> getTopSellers();

    // TOP AUCTIONS
    List<Auction>
    findTop5ByOrderByCurrentHighestBidDesc();

    // TOTAL REVENUE
    @Query("""
           SELECT COALESCE(
               SUM(a.currentHighestBid),
               0
           )
           FROM Auction a
           WHERE a.status IN (
               'ACTIVE',
               'ENDED'
           )
           """)
    Double getTotalRevenue();

    // ACTIVE REVENUE
    @Query("""
           SELECT COALESCE(
               SUM(a.currentHighestBid),
               0
           )
           FROM Auction a
           WHERE a.status = 'ACTIVE'
           """)
    Double getActiveAuctionRevenue();

    // ENDED REVENUE
    @Query("""
           SELECT COALESCE(
               SUM(a.currentHighestBid),
               0
           )
           FROM Auction a
           WHERE a.status = 'ENDED'
           """)
    Double getEndedAuctionRevenue();
}