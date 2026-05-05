package com.auction.itemservice.controller;

import com.auction.itemservice.dto.ItemRequestDto;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // ✅ CREATE ITEM
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Item> createItem(
            @RequestPart("data") @Valid ItemRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("userId") Long sellerId
    ) {
        return ResponseEntity.ok(itemService.createItem(dto, sellerId, image));
    }

    // ✅ UPDATE ITEM (FIXED)
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Item> updateItem(
            @PathVariable Long id,
            @RequestPart("data") @Valid ItemRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("userId") Long sellerId
    ) {
        return ResponseEntity.ok(itemService.updateItem(id, dto, sellerId, image));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(
            @PathVariable Long id,
            @RequestHeader("userId") Long sellerId
    ) {
        itemService.deleteItem(id, sellerId);
        return ResponseEntity.ok("Item deleted successfully");
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Item>> getAll() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // ✅ GET BY CATEGORY
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Item>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category));
    }
}
