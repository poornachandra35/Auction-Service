package com.auction.notification_service.service;

import com.auction.notification_service.dto.NotificationEvent;

public interface NotificationService {

    void consume(NotificationEvent event);
}