package com.auction.userservice.filter;

import jakarta.servlet.FilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import org.slf4j.MDC;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter =
            new CorrelationIdFilter();

    // ================= EXISTING HEADER =================

    @Test
    void testExistingCorrelationId()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain =
                mock(FilterChain.class);

        request.addHeader(
                "X-Correlation-Id",
                "test-id"
        );

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertEquals(
                "test-id",

                response.getHeader(
                        "X-Correlation-Id"
                )
        );

        verify(filterChain, times(1))
                .doFilter(request, response);

        assertNull(
                MDC.get("X-Correlation-Id")
        );
    }

    // ================= GENERATE NEW HEADER =================

    @Test
    void testGenerateCorrelationId()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain =
                mock(FilterChain.class);

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        String generatedId =
                response.getHeader(
                        "X-Correlation-Id"
                );

        assertNotNull(generatedId);

        assertFalse(
                generatedId.isBlank()
        );

        verify(filterChain, times(1))
                .doFilter(request, response);

        assertNull(
                MDC.get("X-Correlation-Id")
        );
    }

    // ================= BLANK HEADER =================

    @Test
    void testBlankCorrelationId()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain =
                mock(FilterChain.class);

        request.addHeader(
                "X-Correlation-Id",
                ""
        );

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        String generatedId =
                response.getHeader(
                        "X-Correlation-Id"
                );

        assertNotNull(generatedId);

        assertFalse(
                generatedId.isBlank()
        );

        verify(filterChain, times(1))
                .doFilter(request, response);
    }

    // ================= FILTER CHAIN EXCEPTION =================

    @Test
    void testFilterChainException()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain =
                mock(FilterChain.class);

        doThrow(new IOException("Filter Error"))
                .when(filterChain)
                .doFilter(request, response);

        assertThrows(
                IOException.class,
                () -> filter.doFilterInternal(
                        request,
                        response,
                        filterChain
                )
        );

        assertNull(
                MDC.get("X-Correlation-Id")
        );
    }
}