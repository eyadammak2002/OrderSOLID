package com.eya.order.entity;

import com.eya.order.domain.CustomerType;
import jakarta.persistence.*;

// ✅ Les infos client sont regroupées au lieu d'être mélangées dans Order
@Embeddable
public class Customer {

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_email")
    private String email;

    @Column(name = "customer_phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType type;

    public Customer() {}

    public Customer(String name, String email, String phone, CustomerType type) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public CustomerType getType() { return type; }
}