package com.auction.itemservice.client;

import com.auction.itemservice.dto.NotificationEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notification-service", url = "http://localhost:8083")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationEvent event);
}