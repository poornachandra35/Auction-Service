package com.payment_service.dto;

import lombok.Data;

@Data
public class PaymentVerificationRequest {

    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String razorpaySignature;

    private Long auctionId;

    private Long winnerId;
}