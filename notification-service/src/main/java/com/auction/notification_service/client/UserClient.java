package com.auction.notification_service.client;

import com.auction.notification_service.dto.BuyerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/users/filter")
    List<BuyerDto> filterBuyers(
            @RequestParam String category,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String location
    );
}