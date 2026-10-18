package com.eya.order.notification;

import com.eya.order.domain.NotificationType;
import com.eya.order.entity.Order;

public interface NotificationChannel {
    NotificationType type();
    void send(Order order, String message);
}