package com.namrata.microservices.productservice.controller;

import com.namrata.microservices.productservice.dto.ApiResponse;
import com.namrata.microservices.productservice.dto.ProductRequest;
import com.namrata.microservices.productservice.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.namrata.microservices.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product APIs", description = "Operations related to products")
public class ProductController {
    private final ProductService service;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }

    @PostMapping
    @Operation(summary = "Add Product", description = "Creates a new product")
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = service.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ProductResponse>builder()
                                .success(true)
                                .message("Product created successfully")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @Operation(summary = "Get All Products", description = "Fetches all products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> response = service.getAllProducts();
        return ResponseEntity.ok(
                ApiResponse.<List<ProductResponse>>builder()
                        .success(true)
                        .message("Product fetched successfully")
                        .data(response)
                        .totalRecords(response.size())
                        .build()
                );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product By ID", description = "Fetches product details by id")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        ProductResponse response = service.getProduct(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .success(true)
                        .message("Product fetched successfully")
                        .data(response)
                        .totalRecords(1)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product", description = "Updates existing product")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductResponse response = service.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .success(true)
                        .message("Product updated successfully")
                        .data(response)
                        .totalRecords(1)
                        .build()
        );
    }
}
