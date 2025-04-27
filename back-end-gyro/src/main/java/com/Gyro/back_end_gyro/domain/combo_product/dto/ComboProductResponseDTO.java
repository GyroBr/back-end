package com.Gyro.back_end_gyro.domain.combo_product.dto;

import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import com.Gyro.back_end_gyro.domain.product.dto.ProductResponseDTO;

public record ComboProductResponseDTO(
        Long id,
        Integer productQuantity,
        Double rawPrice,
        ProductResponseDTO product
) {

    public ComboProductResponseDTO(ComboProduct comboProduct) {
        this(
                comboProduct.getId(),
                comboProduct.getProductQuantity(),
                comboProduct.getRawPrice(),
                new ProductResponseDTO(comboProduct.getProduct())


        );
    }
}
