package com.eya.order.payment;

public class CashPayment extends PaymentMethod {

    @Override
    public double getFeeRate() {
        return 0;
    }

    @Override
    public void pay(double amount) {
        // ❌ LSP : ici "pay" n'encaisse rien, alors que dans les autres classes
        //    "pay" veut dire "argent encaissé". Le sens de la méthode change.
        System.out.println("Paiement à la livraison de " + amount + " DT (non encaissé)");
    }

    @Override
    public void refund(double amount) {
        // ❌ LSP : la sous-classe REFUSE le contrat de la classe mère.
        //    Le code qui appelle refund() sur un PaymentMethod va planter.
        throw new UnsupportedOperationException("Le paiement cash ne peut pas être remboursé");
    }
}