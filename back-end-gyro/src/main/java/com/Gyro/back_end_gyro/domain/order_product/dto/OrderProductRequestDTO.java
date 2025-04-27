package com.Gyro.back_end_gyro.domain.order_product.dto;

import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderProductRequestDTO(

        @NotNull
        Long productId,

        @NotNull
        Long orderId,

        @NotNull
        @Positive
        Integer orderQuantity



) {


}
