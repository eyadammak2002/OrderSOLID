package com.eya.order.service;

import com.eya.order.dto.ProductRequest;
import com.eya.order.dto.ProductResponse;
import com.eya.order.entity.Product;
import com.eya.order.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse create(ProductRequest request) {
        if (request.price() <= 0) {
            throw new RuntimeException("Le prix doit être supérieur à 0");
        }
        if (request.stock() < 0) {
            throw new RuntimeException("Le stock ne peut pas être négatif");
        }
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        return ProductResponse.from(productRepository.save(product));
    }
}