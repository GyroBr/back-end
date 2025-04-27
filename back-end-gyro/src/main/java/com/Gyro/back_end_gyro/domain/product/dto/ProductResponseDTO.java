package com.Gyro.back_end_gyro.domain.product.dto;

import com.Gyro.back_end_gyro.domain.product.entity.Product;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ProductResponseDTO(
        Long productId,
        Integer totalSales,
        String name,
        Double price,
        String volume,
        Integer quantity,
        String image,
        LocalDate expiresAt,
        Integer warningQuantity,
        String barCode,
        String category
) {

    public ProductResponseDTO(Product product) {
        this(product.getId(), product.getTotalSales(), product.getName(), product.getPrice(), product.getVolume(), product.getQuantity(), product.getImage(), product.getExpiresAt(), product.getWarningQuantity(), product.getBarCode(), product.getCategory());
    }
}
