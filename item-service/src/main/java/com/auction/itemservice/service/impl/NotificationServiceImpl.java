package com.auction.itemservice.service.impl;

import com.auction.itemservice.client.NotificationClient;
import com.auction.itemservice.client.UserClient;
import com.auction.itemservice.dto.NotificationEvent;
import com.auction.itemservice.dto.UserPreferenceResponseDto;
import com.auction.itemservice.entity.Item;
import com.auction.itemservice.service.NotificationService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl
        implements NotificationService {

    private final UserClient userClient;

    private final NotificationClient notificationClient;

    @Override
    @CircuitBreaker(
            name = "notificationService",
            fallbackMethod = "notificationFallback"
    )
    @Retry(name = "notificationService")
    public void notifyInterestedBuyers(
            Item item
    ) {

        log.info(
                "Sending notifications for item: {}",
                item.getId()
        );

        List<UserPreferenceResponseDto> buyers =
                userClient.filterBuyers(
                        item.getCategory(),
                        item.getBasePrice(),
                        item.getBasePrice() + 10000,
                        "Bangalore"
                );

        String message =
                "New item available: "
                        + item.getTitle();

        for (UserPreferenceResponseDto buyer : buyers) {

            NotificationEvent event =
                    new NotificationEvent(
                            String.valueOf(
                                    buyer.getUserId()
                            ),
                            message,
                            buyer.getEmail()
                    );

            notificationClient.sendNotification(
                    event
            );
        }

        log.info(
                "Notifications sent successfully"
        );
    }

    // =====================================================
    // FALLBACK
    // =====================================================

    public void notificationFallback(
            Item item,
            Exception ex
    ) {

        log.error(
                "Notification fallback executed for item {}",
                item.getId(),
                ex
        );
    }
}