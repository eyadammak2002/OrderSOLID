
package com.eya.order.discount;

import org.springframework.stereotype.Component;

import com.eya.order.domain.CustomerType;

@Component
public class RegularDiscount implements DiscountStrategy {

    public CustomerType customerType() {
        return CustomerType.REGULAR;
    }

    public double rate() {
        return 0;
    }
}
