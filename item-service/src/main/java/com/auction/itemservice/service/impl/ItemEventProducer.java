package com.auction.itemservice.service.impl;

import com.auction.itemservice.dto.ItemCreatedEvent;
import com.auction.itemservice.entity.Item;

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
public class ItemEventProducer {

    private final KafkaTemplate<
            String,
            ItemCreatedEvent
            > kafkaTemplate;

    private static final String TOPIC =
            "item-created-topic";

    public void publishItemCreatedEvent(
            Item item
    ) {

        ItemCreatedEvent event =
                ItemCreatedEvent.builder()
                        .itemId(item.getId())
                        .title(item.getTitle())
                        .category(item.getCategory())
                        .basePrice(item.getBasePrice())
                        .build();

        ProducerRecord<
                String,
                ItemCreatedEvent
                > record =
                new ProducerRecord<>(
                        TOPIC,
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
                "Item created event published successfully for itemId: {}",
                item.getId()
        );
    }
}