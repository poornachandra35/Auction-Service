package com.auction.notification_service.service;

import com.auction.notification_service.client.UserClient;

import com.auction.notification_service.dto.BuyerDto;
import com.auction.notification_service.dto.ItemCreatedEvent;

import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.apache.kafka.common.header.Header;

import org.slf4j.MDC;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final UserClient userClient;

    private final EmailService emailService;

    @KafkaListener(
            topics = "item-created-topic",
            groupId = "notification-group"
    )
    @Retry(name = "notificationRetry")
    public void consume(
            ConsumerRecord<
                    String,
                    ItemCreatedEvent
                    > record
    ) {

        Header correlationHeader =
                record.headers()
                        .lastHeader(
                                "X-Correlation-Id"
                        );

        if (correlationHeader != null) {

            String correlationId =
                    new String(
                            correlationHeader.value(),
                            StandardCharsets.UTF_8
                    );

            MDC.put(
                    "X-Correlation-Id",
                    correlationId
            );
        }

        ItemCreatedEvent event =
                record.value();

        log.info(
                "Received item created event for itemId: {}",
                event.getItemId()
        );

        List<BuyerDto> buyers =
                userClient.filterBuyers(
                        event.getCategory(),
                        event.getBasePrice(),
                        event.getBasePrice() + 10000,
                        "Bangalore"
                );

        String message =
                "New item available: "
                        + event.getTitle();

        for (BuyerDto buyer : buyers) {

            try {

                emailService.sendEmail(
                        buyer.getEmail(),
                        message
                );

                log.info(
                        "Notification sent successfully to: {}",
                        buyer.getEmail()
                );

            } catch (Exception ex) {

                log.error(
                        "Email failed for: {}",
                        buyer.getEmail(),
                        ex
                );

                throw ex;
            }
        }

        MDC.clear();
    }
}