package com.eya.order.notification;

import com.eya.order.domain.NotificationType;
import com.eya.order.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class SmsChannel implements NotificationChannel {
    public NotificationType type() { return NotificationType.SMS;}

    public void send(Order order, String message) {
        System.out.println("Envoi d'un SMS à " + order.getCustomer().getPhone() + " : " + message);
    }
}