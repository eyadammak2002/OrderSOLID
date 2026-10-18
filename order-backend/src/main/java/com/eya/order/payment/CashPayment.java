package com.eya.order.payment;

import org.springframework.stereotype.Component;

import com.eya.order.domain.OrderStatus;
import com.eya.order.domain.PaymentType;

@Component
public class CashPayment implements PaymentMethod {
    public PaymentType type() { return PaymentType.CASH; }
    public double feeRate() { return 0; }

    public OrderStatus pay(double amount) {
        System.out.println("Paiement à la livraison de " + amount + " DT");
        return OrderStatus.PENDING; 
    }

    //  Plus d'UnsupportedOperationException : "rien à rembourser" = ne rien faire
    public void refund(double amount) {
        System.out.println("Aucun remboursement (paiement non encaissé)");
    }
}