package com.eya.order.controller;

import com.eya.order.entity.Product;
import com.eya.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")   // ❌ CORS ouvert à tout le monde
public class ProductController {

    // ❌ Le controller parle directement au repository (pas de couche service)
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {   // ❌ Entité exposée, pas de DTO
        return productRepository.save(product);
    }
}