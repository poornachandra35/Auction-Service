package com.auction.itemservice.service;

import com.auction.itemservice.client.*;
import com.auction.itemservice.dto.*;
import com.auction.itemservice.entity.*;
import com.auction.itemservice.repository.ItemRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private AuctionClient auctionClient;

    @InjectMocks
    private ItemService itemService;

    @Test
    void testCreateItemSuccess() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setTitle("Laptop");
        dto.setCategory("Electronics");
        dto.setBasePrice(50000.0);
        dto.setAuctionStartTime(LocalDateTime.now().plusMinutes(10));
        dto.setAuctionDurationMinutes(10);

        UserDto user = new UserDto();
        user.setRole("SELLER");

        when(userClient.getUserById(1L)).thenReturn(user);

        Item savedItem = Item.builder()
                .id(1L)
                .title("Laptop")
                .category("Electronics")
                .basePrice(50000.0)
                .sellerId(1L)
                .build();

        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);
        when(userClient.filterBuyers(any(), any(), any(), any()))
                .thenReturn(List.of());

        Item result = itemService.createItem(dto, 1L);

        assertEquals("Laptop", result.getTitle());
        verify(itemRepository).save(any(Item.class));
        verify(auctionClient).createAuction(any());
    }

    @Test
    void testCreateItemInvalidRole() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setAuctionStartTime(LocalDateTime.now().plusMinutes(10));
        dto.setAuctionDurationMinutes(10);

        UserDto user = new UserDto();
        user.setRole("BUYER");

        when(userClient.getUserById(1L)).thenReturn(user);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> itemService.createItem(dto, 1L));

        assertEquals("Only sellers can upload items", ex.getMessage());
    }

    @Test
    void testGetItemById() {
        Item item = Item.builder().id(1L).title("Laptop").build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.getItemById(1L);

        assertEquals("Laptop", result.getTitle());
    }

    @Test
    void testGetAllItems() {
        when(itemRepository.findAll()).thenReturn(List.of());

        List<Item> items = itemService.getAllItems();

        assertNotNull(items);
    }

    @Test
    void testGetItemsByCategory() {
        when(itemRepository.findByCategory("Electronics"))
                .thenReturn(List.of());

        List<Item> items = itemService.getItemsByCategory("Electronics");

        assertNotNull(items);
    }
}