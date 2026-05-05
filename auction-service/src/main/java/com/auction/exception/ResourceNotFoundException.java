package com.auction.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;  // ✅ FIX

    public ResourceNotFoundException(String message) {
        super(message);
    }
}