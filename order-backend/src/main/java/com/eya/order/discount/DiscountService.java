package com.eya.order.discount;

import com.eya.order.domain.CustomerType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscountService {

    private final Map<CustomerType, DiscountStrategy> strategies = new EnumMap<>(CustomerType.class);

    public DiscountService(List<DiscountStrategy> all) {
        all.forEach(s -> strategies.put(s.customerType(), s));
    }

    public double discountFor(CustomerType type, double subtotal) {
        DiscountStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new RuntimeException("Type de client inconnu");
        }
        return subtotal * strategy.rate();
    }
}