package com.eya.order.service;

import com.eya.order.entity.Order;
import com.eya.order.entity.OrderItem;
import com.eya.order.entity.Product;
import com.eya.order.notification.EmailNotifier;
import com.eya.order.notification.NotificationChannel;
import com.eya.order.notification.SmsNotifier;
import com.eya.order.payment.CashPayment;
import com.eya.order.payment.CreditCardPayment;
import com.eya.order.payment.PaymentMethod;
import com.eya.order.payment.PaypalPayment;
import com.eya.order.repository.OrderRepository;
import com.eya.order.repository.ProductRepository;
import com.eya.order.util.AuditLogger;
import com.eya.order.util.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    // ❌ DIP : injection par champ, dépendances cachées, pas testable sans Spring
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // ❌ SRP : une seule méthode de ~100 lignes qui fait TOUT
    @Transactional
    public Order createOrder(Order order) {

        // ❌ DIP : dépendance à un Singleton global (impossible à remplacer en test)
        AuditLogger.getInstance().log("Création commande pour " + order.getCustomerName());

        // --- Validation ---
        if (order.getCustomerName() == null || order.getCustomerName().isEmpty()) {
            throw new RuntimeException("Nom du client obligatoire");
        }
        if (order.getItems() == null || order.getItems().size() == 0) {
            throw new RuntimeException("La commande est vide");
        }
        // ❌ On ne valide pas quantity > 0 : une quantité négative AUGMENTE le stock

        // --- Stock et sous-total ---
        double subtotal = 0;
        for (OrderItem item : order.getItems()) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));
            if (p.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock insuffisant pour " + p.getName());
            }
            p.setStock(p.getStock() - item.getQuantity());
            productRepository.save(p);

            item.setProductName(p.getName());
            item.setUnitPrice(p.getPrice());
            subtotal += p.getPrice() * item.getQuantity();
        }

        // --- Remise ---
        // ❌ OCP : pour ajouter un type de client, on MODIFIE ce code
        double discount = 0;
        if ("VIP".equals(order.getCustomerType())) {
            discount = subtotal * 0.20;
        } else if ("STUDENT".equals(order.getCustomerType())) {
            discount = subtotal * 0.10;
        } else if ("EMPLOYEE".equals(order.getCustomerType())) {
            discount = subtotal * 0.30;
        } else if ("REGULAR".equals(order.getCustomerType())) {
            discount = 0;
        } else {
            throw new RuntimeException("Type de client inconnu");
        }
        order.setDiscount(discount);
        double total = subtotal - discount;

        // --- TVA ---
        double tax = PriceUtils.vat(total);   // ❌ méthode statique, non mockable
        order.setTax(tax);
        total = total + tax;

        // --- Livraison ---
        // ❌ Valeurs magiques
        double shipping = 0;
        if (total < 100) {
            shipping = 7;
        }
        order.setShipping(shipping);
        total = total + shipping;

        // --- Choix du moyen de paiement ---
        // ❌ OCP + DIP : on crée les classes concrètes avec "new" dans un if/else.
        //    Ajouter APPLE_PAY = modifier ici ET dans cancelOrder.
        PaymentMethod payment;
        if ("CREDIT_CARD".equals(order.getPaymentMethod())) {
            payment = new CreditCardPayment();
        } else if ("PAYPAL".equals(order.getPaymentMethod())) {
            payment = new PaypalPayment();
        } else if ("CASH".equals(order.getPaymentMethod())) {
            payment = new CashPayment();
        } else {
            throw new RuntimeException("Moyen de paiement inconnu");
        }

        double fee = total * payment.getFeeRate();
        order.setPaymentFee(fee);
        total = total + fee;

        payment.pay(total);

        // ❌ LSP : on est obligé de tester le type concret. Si le polymorphisme
        //    marchait vraiment, on n'aurait jamais besoin de instanceof.
        if (payment instanceof CashPayment) {
            order.setStatus("PENDING");
        } else {
            order.setStatus("PAID");
        }

        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        // --- Notification ---
        // ❌ OCP + DIP + ISP : "new" concret, et on doit connaître la bonne méthode
        //    de l'interface fourre-tout pour chaque canal
        NotificationChannel channel;
        if ("EMAIL".equals(order.getNotificationType())) {
            channel = new EmailNotifier();
            channel.sendEmail(order.getCustomerEmail(), "Commande " + saved.getId() + " confirmée");
        } else if ("SMS".equals(order.getNotificationType())) {
            channel = new SmsNotifier();
            channel.sendSms(order.getCustomerPhone(), "Commande " + saved.getId() + " confirmée");
        }

        AuditLogger.getInstance().log("Commande " + saved.getId() + " créée, total " + total);
        return saved;
    }

    @Transactional
    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Commande déjà annulée");
        }

        // Remise en stock
        for (OrderItem item : order.getItems()) {
            Product p = productRepository.findById(item.getProductId()).get();
            p.setStock(p.getStock() + item.getQuantity());
            productRepository.save(p);
        }

        // ❌ Copier-coller du if/else de createOrder (duplication)
        PaymentMethod payment;
        if ("CREDIT_CARD".equals(order.getPaymentMethod())) {
            payment = new CreditCardPayment();
        } else if ("PAYPAL".equals(order.getPaymentMethod())) {
            payment = new PaypalPayment();
        } else if ("CASH".equals(order.getPaymentMethod())) {
            payment = new CashPayment();
        } else {
            throw new RuntimeException("Moyen de paiement inconnu");
        }

        // ❌ LSP : sans ce instanceof, payment.refund() lèverait
        //    UnsupportedOperationException pour le cash
        if (payment instanceof CashPayment) {
            System.out.println("Aucun remboursement (paiement non encaissé)");
        } else {
            payment.refund(order.getTotal());
        }

        order.setStatus("CANCELLED");

        // ❌ Notification copiée-collée
        NotificationChannel channel;
        if ("EMAIL".equals(order.getNotificationType())) {
            channel = new EmailNotifier();
            channel.sendEmail(order.getCustomerEmail(), "Commande " + order.getId() + " annulée");
        } else if ("SMS".equals(order.getNotificationType())) {
            channel = new SmsNotifier();
            channel.sendSms(order.getCustomerPhone(), "Commande " + order.getId() + " annulée");
        }

        AuditLogger.getInstance().log("Commande " + order.getId() + " annulée");
        return orderRepository.save(order);
    }
}