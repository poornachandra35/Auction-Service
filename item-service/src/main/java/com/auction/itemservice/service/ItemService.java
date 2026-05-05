package com.auction.itemservice.service;

import com.auction.itemservice.client.AuctionClient;
import com.auction.itemservice.client.NotificationClient;
import com.auction.itemservice.client.UserClient;
import com.auction.itemservice.dto.*;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.entity.ItemStatus;
import com.auction.itemservice.exception.*;
import com.auction.itemservice.repository.ItemRepository;
import com.auction.itemservice.util.FileUploadUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;
    private final AuctionClient auctionClient;
    private final FileUploadUtil fileUploadUtil;

    public Item createItem(ItemRequestDto dto, Long sellerId, MultipartFile image) {

        UserDto user = userClient.getUserById(sellerId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {
            throw new UnauthorizedException("Only sellers can upload items");
        }

        if (dto.getAuctionStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Auction start time must be in the future");
        }

        if (dto.getAuctionDurationMinutes() < 5) {
            throw new BadRequestException("Minimum auction duration is 5 minutes");
        }

        LocalDateTime startTime = dto.getAuctionStartTime();
        LocalDateTime endTime = startTime.plusMinutes(dto.getAuctionDurationMinutes());

        String imagePath = fileUploadUtil.saveFile(image);

        Item item = Item.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .basePrice(dto.getBasePrice())
                .auctionStartTime(startTime)
                .auctionEndTime(endTime)
                .sellerId(sellerId)
                .status(ItemStatus.CREATED)
                .imageUrl(imagePath)
                .build();

        Item savedItem = itemRepository.save(item);

        try {
            AuctionRequest request = new AuctionRequest();
            request.setItemId(savedItem.getId());
            request.setSellerId(savedItem.getSellerId());
            request.setBasePrice(savedItem.getBasePrice());
            request.setStartTime(startTime);
            request.setEndTime(endTime);

            auctionClient.createAuction(request);

        } catch (Exception e) {
            System.out.println("Auction service failed: " + e.getMessage());
        }

        try {
            List<UserPreferenceResponseDto> buyers = userClient.filterBuyers(
                    savedItem.getCategory(),
                    savedItem.getBasePrice(),
                    savedItem.getBasePrice() + 10000,
                    "Bangalore"
            );

            String message = "New item available: " + savedItem.getTitle() +
                    " | Category: " + savedItem.getCategory() +
                    " | Price: ₹" + savedItem.getBasePrice();

            for (UserPreferenceResponseDto buyer : buyers) {

                NotificationEvent event = new NotificationEvent(
                        String.valueOf(buyer.getUserId()),
                        message,
                        buyer.getEmail()
                );

                notificationClient.sendNotification(event);
            }

        } catch (Exception e) {
            System.out.println("Notification failed: " + e.getMessage());
        }

        return savedItem;
    }

    // ✅ UPDATED METHOD (FIXED + SYNC WITH AUCTION)
    public Item updateItem(Long id, ItemRequestDto dto, Long sellerId, MultipartFile image) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!item.getSellerId().equals(sellerId)) {
            throw new UnauthorizedException("You are not allowed to update this item");
        }

        if (item.getStatus() != ItemStatus.CREATED) {
            throw new BadRequestException("Cannot update item after auction started");
        }

        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setBasePrice(dto.getBasePrice());

        if (image != null && !image.isEmpty()) {
            String imagePath = fileUploadUtil.saveFile(image);
            item.setImageUrl(imagePath);
        }

        if (dto.getAuctionStartTime() != null) {

            if (dto.getAuctionStartTime().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Start time must be in future");
            }

            if (dto.getAuctionDurationMinutes() < 5) {
                throw new BadRequestException("Minimum duration is 5 minutes");
            }

            LocalDateTime startTime = dto.getAuctionStartTime();
            LocalDateTime endTime = startTime.plusMinutes(dto.getAuctionDurationMinutes());

            item.setAuctionStartTime(startTime);
            item.setAuctionEndTime(endTime);
        }

        Item updatedItem = itemRepository.save(item);

        // 🔥 SYNC WITH AUCTION SERVICE (IMPORTANT FIX)
        try {
            AuctionRequest request = new AuctionRequest();
            request.setItemId(updatedItem.getId());
            request.setSellerId(updatedItem.getSellerId());
            request.setBasePrice(updatedItem.getBasePrice());
            request.setStartTime(updatedItem.getAuctionStartTime());
            request.setEndTime(updatedItem.getAuctionEndTime());

            auctionClient.updateAuctionByItemId(updatedItem.getId(), request);

        } catch (Exception e) {
            System.out.println("Auction update failed: " + e.getMessage());
        }

        return updatedItem;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    public void deleteItem(Long id, Long sellerId) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!item.getSellerId().equals(sellerId)) {
            throw new UnauthorizedException("You are not allowed to delete this item");
        }

        if (item.getStatus() != ItemStatus.CREATED) {
            throw new BadRequestException("Cannot delete item after auction started");
        }

        // 🔥 OPTIONAL: DELETE AUCTION ALSO (BEST PRACTICE)
        try {
            auctionClient.deleteByItemId(id);
        } catch (Exception e) {
            System.out.println("Auction delete failed: " + e.getMessage());
        }

        itemRepository.deleteById(id);
    }

    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }
}