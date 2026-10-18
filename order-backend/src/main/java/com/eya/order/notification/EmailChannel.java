package com.eya.order.notification;

import com.eya.order.domain.NotificationType;
import com.eya.order.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class EmailChannel implements NotificationChannel {
    public NotificationType type() { return NotificationType.EMAIL;  }

    public void send(Order order, String message) {
        System.out.println("Envoi d'un email à " +  order.getCustomer().getEmail() + " : " + message);
    }
}