package com.eya.order.dto;

import com.eya.order.entity.Product;

public record ProductResponse(Long id, String name, double price, int stock) {

    public static ProductResponse from(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock());
    }
}