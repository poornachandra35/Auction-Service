package com.auction.userservice.controller;

import com.auction.userservice.dto.UserPreferenceRequestDto;
import com.auction.userservice.dto.UserPreferenceResponseDto;
import com.auction.userservice.dto.UserResponseDto;
import com.auction.userservice.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🔥 REQUIRED FOR ITEM SERVICE
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @PostMapping("/preferences")
    public ResponseEntity<UserPreferenceResponseDto> savePreferences(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody UserPreferenceRequestDto dto
    ) {
        return ResponseEntity.ok(userService.savePreferences(userId, dto));
    }
    
   

    // 🔥 GET PREFERENCES
    @GetMapping("/{id}/preferences")
    public ResponseEntity<UserPreferenceResponseDto> getPreferences(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPreferences(id));
    }

    @GetMapping("/buyers")
    public ResponseEntity<List<UserResponseDto>> getAllBuyers() {
        return ResponseEntity.ok(userService.getAllBuyers());
    }
    // 🔥 FILTER BUYERS (CORE API)
    @GetMapping("/filter")
    public ResponseEntity<List<UserPreferenceResponseDto>> filterBuyers(
            @RequestParam String category,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String location
    ) {
        return ResponseEntity.ok(
                userService.filterBuyers(category, minPrice, maxPrice, location)
        );
    }
}