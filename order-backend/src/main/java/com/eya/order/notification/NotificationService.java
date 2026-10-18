
package com.eya.order.notification;

import com.eya.order.entity.Order;
import com.eya.order.domain.NotificationType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final Map<NotificationType, NotificationChannel> channels =
            new EnumMap<>(NotificationType.class);

    public NotificationService(List<NotificationChannel> all) {
        all.forEach(c -> channels.put(c.type(), c));
    }

    public void orderConfirmed(Order order) {
        new ConfirmedNotification(channelOf(order)).send(order);
    }

    public void orderCancelled(Order order) {
        new CancelledNotification(channelOf(order)).send(order);
    }

    private NotificationChannel channelOf(Order order) {
        NotificationChannel channel = channels.get(order.getNotificationType());
        if (channel == null) {
            throw new RuntimeException("Type de notification inconnu");
        }
        return channel;
    }
}
