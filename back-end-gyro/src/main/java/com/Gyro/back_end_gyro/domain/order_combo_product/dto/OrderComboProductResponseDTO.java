package com.Gyro.back_end_gyro.domain.order_combo_product.dto;

import com.Gyro.back_end_gyro.domain.combo.dto.ComboResponseDTO;
import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.combo_product.dto.ComboProductResponseDTO;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order_combo_product.entity.OrderComboProduct;

import java.util.List;

public record OrderComboProductResponseDTO(
        Long orderId,
        ComboResponseDTO items
) {

    public OrderComboProductResponseDTO(OrderComboProduct orderComboProduct) {
        this(orderComboProduct.getId(), new ComboResponseDTO(orderComboProduct.getComboProduct().getCombo()));
    }


    public OrderComboProductResponseDTO(Order order, Combo combo) {
        this(order.getId(), new ComboResponseDTO(combo));
    }


}
