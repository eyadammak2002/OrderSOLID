package com.eya.order.payment;

import com.eya.order.domain.OrderStatus;
import com.eya.order.domain.PaymentType;

public interface PaymentMethod {
    PaymentType type();
    double feeRate();
    OrderStatus pay(double amount);
    void refund(double amount);
}