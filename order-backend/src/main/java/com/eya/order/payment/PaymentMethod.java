package com.eya.order.payment;

// Classe abstraite : promet que TOUT moyen de paiement sait payer ET rembourser
public abstract class PaymentMethod {

    public abstract double getFeeRate();

    public abstract void pay(double amount);

    // ❌ LSP : ce contrat ("on peut rembourser") n'est pas respecté par toutes les sous-classes
    public abstract void refund(double amount);
}