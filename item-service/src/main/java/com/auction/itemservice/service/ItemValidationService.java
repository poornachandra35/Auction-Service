package com.auction.itemservice.service;

import com.auction.itemservice.dto.ItemRequestDto;
import com.auction.itemservice.dto.UserDto;
import com.auction.itemservice.entity.Item;

public interface ItemValidationService {

    void validateSeller(UserDto user);

    void validateAuction(ItemRequestDto dto);

    void validateItemOwnership(
            Item item,
            Long sellerId
    );

    void validateItemUpdatable(Item item);
}