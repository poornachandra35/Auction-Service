package com.payment_service.entity;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Long auctionId;

    private Long winnerId;

    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String razorpaySignature;

    private String paymentStatus;
}