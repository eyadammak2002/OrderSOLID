package com.eya.order.discount;

import org.springframework.stereotype.Component;

import com.eya.order.domain.CustomerType;

@Component
public class StudentDiscount implements DiscountStrategy {
    public CustomerType customerType() { return CustomerType.STUDENT; }
    public double rate() { return 0.10; }
}