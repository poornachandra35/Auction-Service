package com.auction.itemservice.service.impl;

import com.auction.itemservice.client.UserClient;
import com.auction.itemservice.dto.*;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.entity.ItemStatus;
import com.auction.itemservice.exception.ResourceNotFoundException;
import com.auction.itemservice.repository.ItemRepository;
import com.auction.itemservice.service.*;
import com.auction.itemservice.util.FileUploadUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl
        implements ItemService {

    private final ItemRepository itemRepository;

    private final UserClient userClient;

    private final FileUploadUtil fileUploadUtil;

    private final ItemValidationService validationService;

    private final AuctionIntegrationService auctionService;

    private final ItemEventProducer itemEventProducer;

    @Override
    public Item createItem(
            ItemRequestDto dto,
            Long sellerId,
            MultipartFile image
    ) {

        UserDto user =
                userClient.getUserById(sellerId);

        validationService.validateSeller(user);

        validationService.validateAuction(dto);

        LocalDateTime startTime =
                dto.getAuctionStartTime();

        LocalDateTime endTime =
                startTime.plusMinutes(
                        dto.getAuctionDurationMinutes()
                );

        String imagePath =
                fileUploadUtil.saveFile(image);

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

        Item savedItem =
                itemRepository.save(item);

        auctionService.createAuction(savedItem);

        itemEventProducer
        .publishItemCreatedEvent(savedItem);

        return savedItem;
    }

    @Override
    public Item updateItem(
            Long id,
            ItemRequestDto dto,
            Long sellerId,
            MultipartFile image
    ) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item not found"
                        )
                );

        validationService.validateItemOwnership(
                item,
                sellerId
        );

        validationService.validateItemUpdatable(
                item
        );

        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setBasePrice(dto.getBasePrice());

        if (image != null && !image.isEmpty()) {

            String imagePath =
                    fileUploadUtil.saveFile(image);

            item.setImageUrl(imagePath);
        }

        Item updatedItem =
                itemRepository.save(item);

        auctionService.updateAuction(updatedItem);

        return updatedItem;
    }

    @Override
    public void deleteItem(
            Long id,
            Long sellerId
    ) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item not found"
                        )
                );

        validationService.validateItemOwnership(
                item,
                sellerId
        );

        validationService.validateItemUpdatable(
                item
        );

        auctionService.deleteAuction(id);

        itemRepository.delete(item);
    }

    @Override
    public Item getItemById(Long id) {

        return itemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item not found"
                        )
                );
    }

    @Override
    public Page<Item> getAllItems(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        return itemRepository.findAll(pageable);
    }

    @Override
    public List<Item> getItemsByCategory(
            String category
    ) {

        return itemRepository.findByCategory(
                category
        );
    }

    @Override
    public List<TopCategoryDto> getTopCategories() {

        List<Object[]> results =
                itemRepository.getTopCategories();

        return results.stream()
                .map(obj -> new TopCategoryDto(
                        (String) obj[0],
                        (Long) obj[1]
                ))
                .toList();
    }
}