
package com.auction.itemservice.service.impl;

import com.auction.itemservice.dto.ItemRequestDto;
import com.auction.itemservice.dto.UserDto;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.entity.ItemStatus;
import com.auction.itemservice.exception.*;
import com.auction.itemservice.service.ItemValidationService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ItemValidationServiceImpl
        implements ItemValidationService {

    @Override
    public void validateSeller(UserDto user) {

        if (user == null) {

            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        if (!"SELLER".equalsIgnoreCase(user.getRole())) {

            throw new UnauthorizedException(
                    "Only sellers can upload items"
            );
        }
    }

    @Override
    public void validateAuction(
            ItemRequestDto dto
    ) {

        if (dto.getAuctionStartTime()
                .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "Auction start time must be in future"
            );
        }

        if (dto.getAuctionDurationMinutes() < 5) {

            throw new BadRequestException(
                    "Minimum auction duration is 5 minutes"
            );
        }
    }

    @Override
    public void validateItemOwnership(
            Item item,
            Long sellerId
    ) {

        if (!item.getSellerId().equals(sellerId)) {

            throw new UnauthorizedException(
                    "You are not allowed"
            );
        }
    }

    @Override
    public void validateItemUpdatable(
            Item item
    ) {

        if (item.getStatus() != ItemStatus.CREATED) {

            throw new BadRequestException(
                    "Cannot modify item after auction started"
            );
        }
    }
}