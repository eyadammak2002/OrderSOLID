package com.eya.order.discount;

import com.eya.order.domain.CustomerType;

public interface DiscountStrategy {
    CustomerType customerType();
    double rate();
}