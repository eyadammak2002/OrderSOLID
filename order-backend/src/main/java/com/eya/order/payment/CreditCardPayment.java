package com.eya.order.payment;

public class CreditCardPayment extends PaymentMethod {

    @Override
    public double getFeeRate() {
        return 0.015;   // ❌ valeur codée en dur
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paiement par carte bancaire de " + amount + " DT");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Remboursement carte bancaire de " + amount + " DT");
    }
}