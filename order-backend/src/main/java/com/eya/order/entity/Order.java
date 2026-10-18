package com.eya.order.entity;

import com.eya.order.domain.NotificationType;
import com.eya.order.domain.OrderStatus;
import com.eya.order.domain.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private double discount;
    private double tax;
    private double shipping;
    private double paymentFee;
    private double total;
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public PaymentType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentType paymentMethod) { this.paymentMethod = paymentMethod; }

    public NotificationType getNotificationType() { return notificationType; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getShipping() { return shipping; }
    public void setShipping(double shipping) { this.shipping = shipping; }

    public double getPaymentFee() { return paymentFee; }
    public void setPaymentFee(double paymentFee) { this.paymentFee = paymentFee; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}