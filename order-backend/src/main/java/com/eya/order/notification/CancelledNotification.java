package com.eya.order.notification;

import com.eya.order.entity.Order;

public class CancelledNotification extends OrderNotification {

    public CancelledNotification(NotificationChannel channel) {
        super(channel);
    }

    protected String message(Order order) {
        return "commande " + order.getId() + " annulée";
    }
}