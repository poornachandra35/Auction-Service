package com.payment_service.controller;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.dto.PaymentVerificationRequest;

import com.payment_service.service.PaymentService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService
            paymentService;

    // =====================================
    // CREATE ORDER
    // =====================================

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse>
    createOrder(
            @Valid
            @RequestBody
            PaymentRequest request
    ) throws Exception {

        log.info(
                "Payment order creation started for auctionId: {}",
                request.getAuctionId()
        );

        PaymentResponse response =
                paymentService.createOrder(
                        request
                );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================
    // VERIFY PAYMENT
    // =====================================

    @PostMapping("/verify")
    public ResponseEntity<String>
    verifyPayment(
            @RequestBody
            PaymentVerificationRequest request
    ) {

        log.info(
                "Payment verification started"
        );

        paymentService.verifyPayment(
                request
        );

        return ResponseEntity.ok(
                "Payment Verified Successfully"
        );
    }
}