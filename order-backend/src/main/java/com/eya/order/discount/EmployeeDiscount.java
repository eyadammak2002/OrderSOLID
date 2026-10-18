package com.eya.order.discount;

import org.springframework.stereotype.Component;
import com.eya.order.domain.CustomerType;

@Component
public class EmployeeDiscount implements DiscountStrategy {
    public CustomerType customerType() { return CustomerType.EMPLOYEE; }
    public double rate() { return 0.30; }
}