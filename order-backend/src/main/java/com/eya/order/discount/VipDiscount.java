package com.eya.order.discount;

import org.springframework.stereotype.Component;
import com.eya.order.domain.CustomerType;

@Component
public class VipDiscount implements DiscountStrategy {
    public CustomerType customerType() { return CustomerType.VIP; }
    public double rate() { return 0.20; }
}