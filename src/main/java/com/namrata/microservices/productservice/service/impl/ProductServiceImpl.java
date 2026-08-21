package com.namrata.microservices.productservice.service.impl;

import com.namrata.microservices.productservice.dto.ProductRequest;
import com.namrata.microservices.productservice.dto.ProductResponse;
import com.namrata.microservices.productservice.exception.ProductNotFoundException;
import com.namrata.microservices.productservice.entity.Product;
import org.springframework.stereotype.Service;
import com.namrata.microservices.productservice.repository.ProductRepository;
import com.namrata.microservices.productservice.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product not found with id: " + id));

        existingProduct.setProductName(request.getProductName());
        existingProduct.setPrice(request.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToResponse(existingProduct);
    }

    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .build();
    }
}
