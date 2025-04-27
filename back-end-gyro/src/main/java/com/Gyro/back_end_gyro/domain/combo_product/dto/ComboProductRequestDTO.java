package com.Gyro.back_end_gyro.domain.combo_product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ComboProductRequestDTO(

        @NotNull
        Long comboId,

        @NotNull
        Long productId,

        @NotNull
        @Positive
        Integer productQuantity
        ) {
}
