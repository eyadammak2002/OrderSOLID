package com.eya.order.controller;

import com.eya.order.entity.Order;
import com.eya.order.repository.OrderRepository;
import com.eya.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;   // ❌ service ET repository dans le controller

    @GetMapping
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Order order) {   // ❌ entité JPA en entrée
        try {
            return ResponseEntity.ok(orderService.createOrder(order));
        } catch (Exception e) {   // ❌ catch générique, tout devient 400
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.cancelOrder(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // ❌ Logique métier dans le controller
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        List<Order> orders = orderRepository.findAll();
        double revenue = 0;
        int count = 0;
        for (Order o : orders) {
            if (!"CANCELLED".equals(o.getStatus())) {
                revenue += o.getTotal();
                count++;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("orderCount", count);
        result.put("revenue", revenue);
        return result;
    }
}