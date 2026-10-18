package com.eya.order.payment;

import com.eya.order.domain.PaymentType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentFactory {

    private final Map<PaymentType, PaymentMethod> methods = new EnumMap<>(PaymentType.class);

    public PaymentFactory(List<PaymentMethod> all) {
        all.forEach(m -> methods.put(m.type(), m));
    }

    public PaymentMethod get(PaymentType type) {
        PaymentMethod method = methods.get(type);
        if (method == null) {
            throw new RuntimeException("Moyen de paiement inconnu");
        }
        return method;
    }
}