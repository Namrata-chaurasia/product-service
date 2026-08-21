package com.namrata.microservices.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {
    @Schema(description = "Name of Product", example = "Laptop")
    @NotBlank(message = "Product name is required")
    private String productName;

    @Schema(description = "Price of Product", example = "60000")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;
}
