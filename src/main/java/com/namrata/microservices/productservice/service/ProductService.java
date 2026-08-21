package com.namrata.microservices.productservice.service;

import com.namrata.microservices.productservice.dto.ProductRequest;
import com.namrata.microservices.productservice.dto.ProductResponse;
import com.namrata.microservices.productservice.entity.Product;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest product);

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(Long id);

    ProductResponse updateProduct(Long is, ProductRequest request);
}
