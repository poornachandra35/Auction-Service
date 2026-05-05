package com.auction.itemservice.controller;

import com.auction.itemservice.dto.ItemRequestDto;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateItem() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setTitle("Laptop");
        dto.setCategory("Electronics");
        dto.setBasePrice(50000.0);
        dto.setAuctionStartTime(LocalDateTime.now().plusMinutes(10));
        dto.setAuctionDurationMinutes(10);

        Item item = Item.builder()
                .id(1L)
                .title("Laptop")
                .build();

        when(itemService.createItem(dto, 1L)).thenReturn(item);

        mockMvc.perform(post("/api/items")
                .header("userId", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void testGetAllItems() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of());

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetItemById() throws Exception {
        Item item = Item.builder().id(1L).title("Laptop").build();

        when(itemService.getItemById(1L)).thenReturn(item);

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void testGetByCategory() throws Exception {
        when(itemService.getItemsByCategory("Electronics"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/items/category/Electronics"))
                .andExpect(status().isOk());
    }
}