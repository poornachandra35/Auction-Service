package com.auction.itemservice.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String role; // BUYER / SELLER
}