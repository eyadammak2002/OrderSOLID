
package com.eya.order.controller;

import com.eya.order.dto.CreateOrderRequest;
import com.eya.order.dto.OrderResponse;
import com.eya.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody CreateOrderRequest request) {
        try {
            return ResponseEntity.ok(
                    orderService.createOrder(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(
                            "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    orderService.cancelOrder(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(
                            "message", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        double[] s = orderService.stats();
        Map<String, Object> result = new HashMap<>();
        result.put("orderCount", (int) s[0]);
        result.put("revenue", s[1]);
        return result;
    }
}
