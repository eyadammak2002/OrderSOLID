package com.eya.order.notification;

import com.eya.order.entity.Order;

public abstract class OrderNotification {

    private final NotificationChannel channel;   // ← le pont

    protected OrderNotification(NotificationChannel channel) {
        this.channel = channel;
    }

    protected abstract String message(Order order);

    public void send(Order order) {
        channel.send(order, message(order));
    }
}