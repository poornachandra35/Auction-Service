package com.auction.notification_service.service;

import com.auction.notification_service.dto.AuctionWinnerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.apache.kafka.common.header.Header;

import org.slf4j.MDC;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionWinnerConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "auction-winner-topic",
            groupId = "auction-group"
    )
    public void consume(
            ConsumerRecord<
                    String,
                    AuctionWinnerEvent
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

        AuctionWinnerEvent event =
                record.value();

        log.info(
                "Auction winner event received for winnerId: {}",
                event.getWinnerId()
        );

        // TODO:
        // Fetch winner email from User Service

        String email =
                "winner@gmail.com";

        emailService.sendEmail(
                email,
                event.getMessage()
        );

        log.info(
                "Winner notification sent successfully"
        );

        MDC.clear();
    }
}