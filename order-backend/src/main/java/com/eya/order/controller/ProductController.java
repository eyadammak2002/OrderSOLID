package com.eya.order.controller;

import com.eya.order.dto.ProductRequest;
import com.eya.order.dto.ProductResponse;
import com.eya.order.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest request) {
        try {
            return ResponseEntity.ok(productService.create(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}