package com.api_gateway.filter;

import org.slf4j.MDC;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter
        implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID =
            "X-Correlation-Id";

    @Override
    public int getOrder() {

        return -2;
    }

    @Override
    public Mono<Void> filter(

            ServerWebExchange exchange,

            GatewayFilterChain chain
    ) {

        String correlationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(CORRELATION_ID);

        // =====================================================
        // GENERATE IF MISSING
        // =====================================================

        if (
                correlationId == null
                || correlationId.isBlank()
        ) {

            correlationId =
                    UUID.randomUUID()
                            .toString();
        }

        // =====================================================
        // STORE IN MDC
        // =====================================================

        MDC.put(
                CORRELATION_ID,
                correlationId
        );

        // =====================================================
        // ADD HEADER TO REQUEST
        // =====================================================

        ServerWebExchange modifiedExchange =

                exchange.mutate()

                        .request(

                                exchange.getRequest()

                                        .mutate()

                                        .header(
                                                CORRELATION_ID,
                                                correlationId
                                        )

                                        .build()
                        )

                        .build();

        // =====================================================
        // RESPONSE HEADER
        // =====================================================

        modifiedExchange.getResponse()

                .getHeaders()

                .add(
                        CORRELATION_ID,
                        correlationId
                );

        return chain.filter(modifiedExchange)

                .doFinally(signalType ->
                        MDC.clear()
                );
    }
}