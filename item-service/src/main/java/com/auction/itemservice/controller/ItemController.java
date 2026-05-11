package com.auction.itemservice.controller;

import com.auction.itemservice.dto.ItemRequestDto;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.service.ItemService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // =====================================================
    // CREATE ITEM
    // =====================================================

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Item> createItem(

            @RequestPart("data")
            @Valid
            ItemRequestDto dto,

            @RequestPart(
                    value = "image",
                    required = false
            )
            MultipartFile image,

            @RequestHeader("userId")
            Long sellerId
    ) {

        log.info(
                "Received request to create item by seller: {}",
                sellerId
        );

        Item item =
                itemService.createItem(
                        dto,
                        sellerId,
                        image
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(item);
    }

    // =====================================================
    // UPDATE ITEM
    // =====================================================

    @PutMapping(
            value = "/{id}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<Item> updateItem(

            @PathVariable
            Long id,

            @RequestPart("data")
            @Valid
            ItemRequestDto dto,

            @RequestPart(
                    value = "image",
                    required = false
            )
            MultipartFile image,

            @RequestHeader("userId")
            Long sellerId
    ) {

        log.info(
                "Received request to update item: {}",
                id
        );

        Item updatedItem =
                itemService.updateItem(
                        id,
                        dto,
                        sellerId,
                        image
                );

        return ResponseEntity.ok(updatedItem);
    }

    // =====================================================
    // DELETE ITEM
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(

            @PathVariable
            Long id,

            @RequestHeader("userId")
            Long sellerId
    ) {

        log.info(
                "Received request to delete item: {}",
                id
        );

        itemService.deleteItem(id, sellerId);

        return ResponseEntity.ok(
                "Item deleted successfully"
        );
    }

    // =====================================================
    // GET ALL ITEMS
    // =====================================================

    @GetMapping
    public ResponseEntity<Page<Item>> getAll(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "9"
            )
            int size
    ) {

        log.info(
                "Fetching items with page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                itemService.getAllItems(
                        page,
                        size
                )
        );
    }

    // =====================================================
    // GET ITEM BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(
            @PathVariable Long id
    ) {

        log.info(
                "Fetching item with id: {}",
                id
        );

        return ResponseEntity.ok(
                itemService.getItemById(id)
        );
    }

    // =====================================================
    // GET ITEMS BY CATEGORY
    // =====================================================

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Item>> getByCategory(
            @PathVariable String category
    ) {

        log.info(
                "Fetching items by category: {}",
                category
        );

        return ResponseEntity.ok(
                itemService.getItemsByCategory(
                        category
                )
        );
    }
}