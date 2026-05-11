package com.auction.repository;

import com.auction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByAuctionIdOrderByAmountDesc(Long auctionId);
    List<Bid> findByAuctionId(Long auctionId);
    @Query("""
    	       SELECT b.userId, COUNT(b)
    	       FROM Bid b
    	       GROUP BY b.userId
    	       ORDER BY COUNT(b) DESC
    	       """)
    	List<Object[]> getTopBuyers();
    @Query("""
    		       SELECT DATE(b.timestamp), COUNT(b)
    		       FROM Bid b
    		       GROUP BY DATE(b.timestamp)
    		       ORDER BY DATE(b.timestamp)
    		       """)
    		List<Object[]> getBidTrends();
    		
    		Optional<Bid> findTopByAuctionIdOrderByAmountDesc(
    		        Long auctionId
    		);
}