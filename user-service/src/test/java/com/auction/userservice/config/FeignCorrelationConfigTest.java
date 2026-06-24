package com.auction.userservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.junit.jupiter.api.Test;

import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;

class FeignCorrelationConfigTest {

    private final FeignCorrelationConfig config =
            new FeignCorrelationConfig();

    @Test
    void testRequestInterceptor() {

        MDC.put(
                "X-Correlation-Id",
                "test-correlation-id"
        );

        RequestInterceptor interceptor =
                config.requestInterceptor();

        RequestTemplate template =
                new RequestTemplate();

        interceptor.apply(template);

        assertTrue(
                template.headers()
                        .containsKey(
                                "X-Correlation-Id"
                        )
        );

        assertEquals(
                "test-correlation-id",

                template.headers()
                        .get("X-Correlation-Id")
                        .iterator()
                        .next()
        );

        MDC.clear();
    }
}