package com.auction.itemservice.service;

import com.auction.itemservice.entity.Item;

public interface NotificationService {

    void notifyInterestedBuyers(Item item);
}