package com.eya.order.util;

// ❌ Classe utilitaire statique : non mockable, la TVA est figée dans le code
public class PriceUtils {

    private PriceUtils() {}

    public static double vat(double amount) {
        return amount * 0.19;
    }
}