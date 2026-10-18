package com.eya.order.notification;

import com.eya.order.entity.Order;

public class ConfirmedNotification extends OrderNotification {

    public ConfirmedNotification(NotificationChannel channel) {
        super(channel);
    }

    protected String message(Order order) {
        return "commande " + order.getId() + " confirmée";
    }
}