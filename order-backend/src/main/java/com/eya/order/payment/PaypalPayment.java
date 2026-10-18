package com.eya.order.payment;

import org.springframework.stereotype.Component;

import com.eya.order.domain.OrderStatus;
import com.eya.order.domain.PaymentType;

@Component
public class PaypalPayment implements PaymentMethod {
    public PaymentType type() {return PaymentType.CREDIT_CARD; }
    public double feeRate() { return 0.03; }

    public OrderStatus pay(double amount) {
        System.out.println("Paiement PayPal de " + amount + " DT");
        return OrderStatus.PAID; 
    }

    public void refund(double amount) {
        System.out.println("Remboursement PayPal de " + amount + " DT");
    }
}