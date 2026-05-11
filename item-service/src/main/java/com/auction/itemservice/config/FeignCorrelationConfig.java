package com.auction.itemservice.config;



import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.slf4j.MDC;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignCorrelationConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            String correlationId =
                    MDC.get(
                            "X-Correlation-Id"
                    );

            requestTemplate.header(
                    "X-Correlation-Id",
                    correlationId
            );
        };
    }
}