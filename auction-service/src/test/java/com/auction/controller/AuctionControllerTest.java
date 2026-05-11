package com.auction.controller;

import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import com.auction.entity.Bid;
import com.auction.service.AuctionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionController.class)
class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionServiceImpl auctionServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ CREATE
    @Test
    void testCreateAuction() throws Exception {
        AuctionRequest request = new AuctionRequest();
        request.setItemId(1L);
        request.setSellerId(101L);
        request.setBasePrice(100.0);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusDays(1));

        Auction auction = new Auction();
        auction.setId(1L);

        Mockito.when(auctionServiceImpl.createAuction(Mockito.any()))
                .thenReturn(auction);

        mockMvc.perform(post("/api/auctions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ✅ UPDATE BY ID
    @Test
    void testUpdateAuction() throws Exception {
        AuctionRequest request = new AuctionRequest();

        Auction auction = new Auction();
        auction.setId(1L);

        Mockito.when(auctionServiceImpl.updateAuction(Mockito.eq(1L), Mockito.any()))
                .thenReturn(auction);

        mockMvc.perform(put("/api/auctions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ✅ UPDATE BY ITEM ID
    @Test
    void testUpdateAuctionByItemId() throws Exception {
        AuctionRequest request = new AuctionRequest();

        Auction auction = new Auction();
        auction.setId(1L);

        Mockito.when(auctionServiceImpl.updateAuctionByItemId(Mockito.eq(10L), Mockito.any()))
                .thenReturn(auction);

        mockMvc.perform(put("/api/auctions/by-item/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ✅ DELETE BY ID
    @Test
    void testDeleteAuction() throws Exception {
        mockMvc.perform(delete("/api/auctions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auction deleted successfully"));
    }

    // ✅ DELETE BY ITEM ID
    @Test
    void testDeleteByItemId() throws Exception {
        mockMvc.perform(delete("/api/auctions/by-item/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auction deleted by itemId"));
    }

    // ✅ PLACE BID
    @Test
    void testPlaceBid() throws Exception {
        BidRequest request = new BidRequest();
        request.setAuctionId(1L);
        request.setUserId(10L);
        request.setAmount(200.0);

        Mockito.when(auctionServiceImpl.placeBid(Mockito.any()))
                .thenReturn("Bid placed successfully");

        mockMvc.perform(post("/api/auctions/bid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bid placed successfully"));
    }

    // ✅ GET BY SELLER
    @Test
    void testGetAuctionsBySeller() throws Exception {
        Mockito.when(auctionServiceImpl.getAuctionsBySeller(101L))
                .thenReturn(List.of(new Auction()));

        mockMvc.perform(get("/api/auctions/seller/101"))
                .andExpect(status().isOk());
    }

    // ✅ GET BIDS
    @Test
    void testGetBids() throws Exception {
        Mockito.when(auctionServiceImpl.getBidsByAuction(1L))
                .thenReturn(List.of(new Bid()));

        mockMvc.perform(get("/api/auctions/1/bids"))
                .andExpect(status().isOk());
    }

    // ✅ GET HIGHEST BID
    @Test
    void testGetHighestBid() throws Exception {
        Mockito.when(auctionServiceImpl.getHighestBid(1L))
                .thenReturn(500.0);

        mockMvc.perform(get("/api/auctions/1/highest-bid"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));
    }

    // ✅ GET ALL
    @Test
    void testGetAuctions() throws Exception {
        Mockito.when(auctionServiceImpl.getAuctions(null))
                .thenReturn(List.of(new Auction()));

        mockMvc.perform(get("/api/auctions"))
                .andExpect(status().isOk());
    }

    // ✅ GET BY STATUS
    @Test
    void testGetAuctionsWithStatus() throws Exception {
        Mockito.when(auctionServiceImpl.getAuctions(AuctionStatus.CREATED))
                .thenReturn(List.of(new Auction()));

        mockMvc.perform(get("/api/auctions")
                .param("status", "CREATED"))
                .andExpect(status().isOk());
    }

    // ✅ GET BY ID
    @Test
    void testGetAuctionById() throws Exception {
        Auction auction = new Auction();
        auction.setId(1L);

        Mockito.when(auctionServiceImpl.getAuctionById(1L))
                .thenReturn(auction);

        mockMvc.perform(get("/api/auctions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}