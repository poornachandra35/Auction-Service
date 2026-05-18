package com.auction.notification_service.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.kafka.listener.DefaultErrorHandler;

import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;

import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorConfig {

    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<Object, Object> kafkaTemplate
    ) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate
                );

        FixedBackOff backOff =
                new FixedBackOff(
                        3000L,
                        3
                );

        return new DefaultErrorHandler(
                recoverer,
                backOff
        );
    }
}