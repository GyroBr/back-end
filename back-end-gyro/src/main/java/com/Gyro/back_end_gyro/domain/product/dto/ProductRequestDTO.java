package com.Gyro.back_end_gyro.domain.product.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ProductRequestDTO(

        @NotBlank
        String name,

        @NotNull
        @Positive
        Double price,

        @NotBlank
        String volume,

        @NotNull
        @Positive
        Integer quantity,

        @FutureOrPresent
        LocalDate expiresAt,

        @NotNull
        Integer warningQuantity,

        @NotBlank
        String category,

        String barCode

) {
}
