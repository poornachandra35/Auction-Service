package com.auction.service;

import com.auction.dto.AuctionWinnerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.ProducerRecord;

import org.slf4j.MDC;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionEventProducer {

    private final KafkaTemplate<
            String,
            AuctionWinnerEvent
            > kafkaTemplate;

    private static final String TOPIC =
            "auction-winner-topic";

    public void publishWinnerEvent(
            AuctionWinnerEvent event
    ) {

        ProducerRecord<
                String,
                AuctionWinnerEvent
                > record =
                new ProducerRecord<>(
                        TOPIC,
                        String.valueOf(
                                event.getWinnerId()
                        ),
                        event
                );

        String correlationId =
                MDC.get("X-Correlation-Id");

        if (correlationId != null) {

            record.headers().add(
                    "X-Correlation-Id",
                    correlationId.getBytes(
                            StandardCharsets.UTF_8
                    )
            );
        }

        kafkaTemplate.send(record);

        log.info(
                "Auction winner event published successfully for winnerId: {}",
                event.getWinnerId()
        );
    }
}