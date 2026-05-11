package com.auction.itemservice.controller;

import com.auction.itemservice.dto.TopCategoryDto;
import com.auction.itemservice.service.ItemService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item-admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final ItemService itemService;

    @GetMapping("/top-categories")
    public List<TopCategoryDto> getTopCategories() {

        return itemService.getTopCategories();
    }
}