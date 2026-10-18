package com.eya.order.dto;

import com.eya.order.domain.CustomerType;
import com.eya.order.domain.NotificationType;
import com.eya.order.domain.PaymentType;

import java.util.List;

// Le client ne peut plus envoyer id, status, total... : ces champs n'existent pas ici
public record CreateOrderRequest(
        String customerName,
        String customerEmail,
        String customerPhone,
        CustomerType customerType,
        PaymentType paymentMethod,
        NotificationType notificationType,
        List<OrderItemRequest> items) {}