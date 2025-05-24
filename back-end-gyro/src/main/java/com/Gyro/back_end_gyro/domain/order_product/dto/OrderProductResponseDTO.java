package com.Gyro.back_end_gyro.domain.order_product.dto;

import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import com.Gyro.back_end_gyro.domain.product.dto.ProductResponseDTO;

public record OrderProductResponseDTO(
        ProductResponseDTO product,
        Integer productQuantiy,
        Double priceAtPurchase
) {

    public OrderProductResponseDTO(OrderProduct orderProduct){
        this(new ProductResponseDTO(orderProduct.getProduct()),orderProduct.getOrderQuantity(),orderProduct.getPriceAtPurchase());
    }
}
