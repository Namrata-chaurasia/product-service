package com.namrata.microservices.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    @Schema(description = "Unique Product Id", example = "1")
    private Long id;

    @Schema(description = "Name of Product", example = "Laptop")
    private String productName;

    @Schema(description = "Price of Product", example = "60000")
    private Double price;
}
