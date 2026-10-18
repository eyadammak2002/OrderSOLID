package com.eya.order.payment;

public class PaypalPayment extends PaymentMethod {

    @Override
    public double getFeeRate() {
        return 0.03;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paiement PayPal de " + amount + " DT");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Remboursement PayPal de " + amount + " DT");
    }
}