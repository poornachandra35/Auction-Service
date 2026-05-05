package com.auction.itemservice.client;

import com.auction.itemservice.dto.UserDto;
import com.auction.itemservice.dto.UserPreferenceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/api/users/filter")
    List<UserPreferenceResponseDto> filterBuyers(
            @RequestParam String category,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String location
    );
}