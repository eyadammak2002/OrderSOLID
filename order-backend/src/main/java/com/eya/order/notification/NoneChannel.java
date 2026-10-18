package com.eya.order.notification;

import com.eya.order.domain.NotificationType;
import com.eya.order.entity.Order;
import org.springframework.stereotype.Component;

// "Pas de notification" est un canal comme un autre : plus de if (type != null)
@Component
public class NoneChannel implements NotificationChannel {
    public NotificationType type() { return NotificationType.NONE; }

    public void send(Order order, String message) {
        // volontairement vide
    }
}