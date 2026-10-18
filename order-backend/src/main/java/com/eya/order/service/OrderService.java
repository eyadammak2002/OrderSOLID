package com.eya.order.service;

import com.eya.order.discount.DiscountService;
import com.eya.order.domain.OrderStatus;
import com.eya.order.dto.CreateOrderRequest;
import com.eya.order.dto.OrderItemRequest;
import com.eya.order.dto.OrderResponse;
import com.eya.order.entity.Customer;
import com.eya.order.entity.Order;
import com.eya.order.entity.OrderItem;
import com.eya.order.entity.Product;
import com.eya.order.notification.NotificationService;
import com.eya.order.payment.PaymentFactory;
import com.eya.order.payment.PaymentMethod;
import com.eya.order.repository.OrderRepository;
import com.eya.order.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final double VAT_RATE = 0.19;
    private static final double SHIPPING_COST = 7;
    private static final double FREE_SHIPPING_FROM = 100;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountService discountService;
    private final PaymentFactory paymentFactory;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        DiscountService discountService, PaymentFactory paymentFactory,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
        this.paymentFactory = paymentFactory;
        this.notificationService = notificationService;
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        validate(request);

        Order order = new Order();
        order.setCustomer(new Customer(request.customerName(), request.customerEmail(),
                request.customerPhone(), request.customerType()));
        order.setPaymentMethod(request.paymentMethod());
        order.setNotificationType(request.notificationType());

        double subtotal = reserveStock(request, order);
        double discount = discountService.discountFor(request.customerType(), subtotal);
        double afterDiscount = subtotal - discount;
        double tax = afterDiscount * VAT_RATE;
        double withTax = afterDiscount + tax;
        double shipping = withTax < FREE_SHIPPING_FROM ? SHIPPING_COST : 0;

        PaymentMethod payment = paymentFactory.get(request.paymentMethod());
        double beforeFee = withTax + shipping;
        double fee = beforeFee * payment.feeRate();
        double total = beforeFee + fee;

        order.setDiscount(discount);
        order.setTax(tax);
        order.setShipping(shipping);
        order.setPaymentFee(fee);
        order.setTotal(total);
        order.setStatus(payment.pay(total));
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        notificationService.orderConfirmed(saved);
        return OrderResponse.from(saved);
    }

    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Commande déjà annulée");
        }

        releaseStock(order);
        paymentFactory.get(order.getPaymentMethod()).refund(order.getTotal());
        order.setStatus(OrderStatus.CANCELLED);

        Order saved = orderRepository.save(order);
        notificationService.orderCancelled(saved);
        return OrderResponse.from(saved);
    }

    public double[] stats() {
        List<Order> active = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .toList();
        double revenue = active.stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .mapToDouble(Order::getTotal)
                .sum();
        return new double[]{active.size(), revenue};
    }

    private void validate(CreateOrderRequest request) {
        if (request.customerName() == null || request.customerName().isEmpty()) {
            throw new RuntimeException("Nom du client obligatoire");
        }
        if (request.customerType() == null || request.paymentMethod() == null
                || request.notificationType() == null) {
            throw new RuntimeException("Type de client, paiement et notification obligatoires");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new RuntimeException("La commande est vide");
        }
        for (OrderItemRequest item : request.items()) {
            if (item.productId() == null) {
                throw new RuntimeException("Choisis un produit pour chaque article");
            }
            if (item.quantity() <= 0) {
                throw new RuntimeException("La quantité doit être au moins 1");
            }
        }
    }

    private double reserveStock(CreateOrderRequest request, Order order) {
        double subtotal = 0;
        for (OrderItemRequest req : request.items()) {
            Product p = productRepository.findById(req.productId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));
            if (p.getStock() < req.quantity()) {
                throw new RuntimeException("Stock insuffisant pour " + p.getName());
            }
            p.setStock(p.getStock() - req.quantity());
            productRepository.save(p);

            OrderItem item = new OrderItem();
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setQuantity(req.quantity());
            item.setUnitPrice(p.getPrice());
            order.getItems().add(item);

            subtotal += p.getPrice() * req.quantity();
        }
        return subtotal;
    }

    private void releaseStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));
            p.setStock(p.getStock() + item.getQuantity());
            productRepository.save(p);
        }
    }
}