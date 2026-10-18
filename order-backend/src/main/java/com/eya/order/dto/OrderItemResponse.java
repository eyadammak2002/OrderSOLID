package com.eya.order.dto;

public record OrderItemResponse(Long productId, String productName, int quantity, double unitPrice) {}