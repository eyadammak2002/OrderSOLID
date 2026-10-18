package com.eya.order.dto;

import com.eya.order.domain.CustomerType;
import com.eya.order.domain.NotificationType;
import com.eya.order.domain.OrderStatus;
import com.eya.order.domain.PaymentType;
import com.eya.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        String customerEmail,
        String customerPhone,
        CustomerType customerType,
        PaymentType paymentMethod,
        NotificationType notificationType,
        OrderStatus status,
        double discount,
        double tax,
        double shipping,
        double paymentFee,
        double total,
        LocalDateTime createdAt,
        List<OrderItemResponse> items) {

    // Conversion entité -> DTO
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getCustomer().getName(),
                o.getCustomer().getEmail(),
                o.getCustomer().getPhone(),
                o.getCustomer().getType(),
                o.getPaymentMethod(),
                o.getNotificationType(),
                o.getStatus(),
                o.getDiscount(), o.getTax(), o.getShipping(), o.getPaymentFee(), o.getTotal(),
                o.getCreatedAt(),
                o.getItems().stream()
                        .map(i -> new OrderItemResponse(i.getProductId(), i.getProductName(),
                                i.getQuantity(), i.getUnitPrice()))
                        .toList());
    }
}