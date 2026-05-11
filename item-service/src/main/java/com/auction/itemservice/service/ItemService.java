package com.auction.itemservice.service;

import com.auction.itemservice.dto.ItemRequestDto;
import com.auction.itemservice.dto.TopCategoryDto;
import com.auction.itemservice.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    Item createItem(
            ItemRequestDto dto,
            Long sellerId,
            MultipartFile image
    );

    Item updateItem(
            Long id,
            ItemRequestDto dto,
            Long sellerId,
            MultipartFile image
    );

    void deleteItem(
            Long id,
            Long sellerId
    );

    Item getItemById(Long id);

    Page<Item> getAllItems(
            int page,
            int size
    );

    List<Item> getItemsByCategory(
            String category
    );

    List<TopCategoryDto> getTopCategories();
}