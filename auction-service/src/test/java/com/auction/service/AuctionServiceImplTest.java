package com.auction.service;

import com.auction.client.NotificationClient;
import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;
import com.auction.exception.BadRequestException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceImplTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private AuctionServiceImpl auctionServiceImpl;

    private Auction auction;

    @BeforeEach
    void setup() {
        auction = new Auction();
        auction.setId(1L);
        auction.setItemId(100L);
        auction.setSellerId(200L);
        auction.setBasePrice(1000);
        auction.setCurrentHighestBid(1000);
        auction.setStatus(AuctionStatus.CREATED);
    }

    // ================= CREATE =================
    @Test
    void testCreateAuction() {
        AuctionRequest request = new AuctionRequest();
        request.setItemId(1L);
        request.setSellerId(2L);
        request.setBasePrice(500);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusDays(1));

        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);

        Auction result = auctionServiceImpl.createAuction(request);

        assertNotNull(result);
        verify(auctionRepository).save(any(Auction.class));
    }

    // ================= UPDATE =================
    @Test
    void testUpdateAuctionSuccess() {
        AuctionRequest request = new AuctionRequest();
        request.setBasePrice(2000);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(auctionRepository.save(any())).thenReturn(auction);

        Auction result = auctionServiceImpl.updateAuction(1L, request);

        assertNotNull(result);
        verify(auctionRepository).save(auction);
    }

    @Test
    void testUpdateAuctionInvalidState() {
        auction.setStatus(AuctionStatus.ACTIVE);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        assertThrows(BadRequestException.class,
                () -> auctionServiceImpl.updateAuction(1L, new AuctionRequest()));
    }

    // ================= UPDATE BY ITEM =================
    @Test
    void testUpdateAuctionByItemIdSuccess() {
        AuctionRequest request = new AuctionRequest();

        when(auctionRepository.findByItemId(100L)).thenReturn(Optional.of(auction));
        when(auctionRepository.save(any())).thenReturn(auction);

        Auction result = auctionServiceImpl.updateAuctionByItemId(100L, request);

        assertNotNull(result);
    }

    @Test
    void testUpdateAuctionByItemIdNotFound() {
        when(auctionRepository.findByItemId(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> auctionServiceImpl.updateAuctionByItemId(100L, new AuctionRequest()));
    }

    // ================= DELETE =================
    @Test
    void testDeleteAuctionSuccess() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        auctionServiceImpl.deleteAuction(1L);

        verify(auctionRepository).delete(auction);
    }

    @Test
    void testDeleteAuctionActive() {
        auction.setStatus(AuctionStatus.ACTIVE);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        assertThrows(BadRequestException.class,
                () -> auctionServiceImpl.deleteAuction(1L));
    }

    // ================= DELETE BY ITEM =================
    @Test
    void testDeleteByItemIdSuccess() {
        when(auctionRepository.findByItemId(100L)).thenReturn(Optional.of(auction));

        auctionServiceImpl.deleteByItemId(100L);

        verify(auctionRepository).delete(auction);
    }

    // ================= GET =================
    @Test
    void testGetAllAuctions() {
        when(auctionRepository.findAll()).thenReturn(List.of(auction));

        List<Auction> result = auctionServiceImpl.getAuctions(null);

        assertEquals(1, result.size());
    }

    @Test
    void testGetAuctionsByStatus() {
        when(auctionRepository.findByStatus(AuctionStatus.CREATED))
                .thenReturn(List.of(auction));

        List<Auction> result = auctionServiceImpl.getAuctions(AuctionStatus.CREATED);

        assertEquals(1, result.size());
    }

    @Test
    void testGetAuctionByIdSuccess() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        Auction result = auctionServiceImpl.getAuctionById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAuctionByIdNotFound() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> auctionServiceImpl.getAuctionById(1L));
    }

    // ================= START =================
    @Test
    void testStartAuctionSuccess() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        auctionServiceImpl.startAuction(1L);

        assertEquals(AuctionStatus.ACTIVE, auction.getStatus());
        verify(auctionRepository).save(auction);
    }

    @Test
    void testStartAuctionInvalidState() {
        auction.setStatus(AuctionStatus.ACTIVE);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        assertThrows(BadRequestException.class,
                () -> auctionServiceImpl.startAuction(1L));
    }

    // ================= BID =================
    @Test
    void testPlaceBidSuccess() {
        auction.setStatus(AuctionStatus.ACTIVE);

        BidRequest request = new BidRequest();
        request.setAuctionId(1L);
        request.setUserId(10L);
        request.setAmount(1500);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        String result = auctionServiceImpl.placeBid(request);

        assertEquals("Bid placed successfully", result);
        verify(bidRepository).save(any(Bid.class));
        verify(auctionRepository).save(auction);
    }

    @Test
    void testPlaceBidLowerAmount() {
        auction.setStatus(AuctionStatus.ACTIVE);

        BidRequest request = new BidRequest();
        request.setAuctionId(1L);
        request.setUserId(10L);
        request.setAmount(500);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        assertThrows(BadRequestException.class,
                () -> auctionServiceImpl.placeBid(request));
    }

    @Test
    void testPlaceBidNotActive() {
        auction.setStatus(AuctionStatus.CREATED);

        BidRequest request = new BidRequest();
        request.setAuctionId(1L);
        request.setUserId(10L);
        request.setAmount(1500);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        assertThrows(BadRequestException.class,
                () -> auctionServiceImpl.placeBid(request));
    }

    // ================= BIDS =================
    @Test
    void testGetBidsByAuction() {
        when(bidRepository.findByAuctionId(1L))
                .thenReturn(List.of(new Bid()));

        List<Bid> bids = auctionServiceImpl.getBidsByAuction(1L);

        assertEquals(1, bids.size());
    }

    // ================= HIGHEST BID =================
    @Test
    void testGetHighestBid() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        Double highest = auctionServiceImpl.getHighestBid(1L);

        assertEquals(1000, highest);
    }

    // ================= SELLER =================
    @Test
    void testGetAuctionsBySeller() {
        when(auctionRepository.findBySellerId(200L))
                .thenReturn(List.of(auction));

        List<Auction> result = auctionServiceImpl.getAuctionsBySeller(200L);

        assertEquals(1, result.size());
    }

    // ================= END =================
    @Test
    void testEndAuctionSuccess() {
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setHighestBidderId(10L);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        auctionServiceImpl.endAuction(1L);

        assertEquals(AuctionStatus.ENDED, auction.getStatus());
        verify(notificationClient).sendNotification(any());
    }

    @Test
    void testEndAuctionNoWinner() {
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setHighestBidderId(null);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        auctionServiceImpl.endAuction(1L);

        verify(notificationClient, never()).sendNotification(any());
    }

    @Test
    void testEndAuctionNotActive() {
        auction.setStatus(AuctionStatus.CREATED);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        assertThrows(BadRequestException.class,
                () -> auctionServiceImpl.endAuction(1L));
    }
}