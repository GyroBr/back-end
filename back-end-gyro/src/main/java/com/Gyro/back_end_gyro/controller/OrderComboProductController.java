package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.combo.service.ComboService;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order.service.OrderService;
import com.Gyro.back_end_gyro.domain.order_combo_product.dto.OrderComboProductRequestDTO;
import com.Gyro.back_end_gyro.domain.order_combo_product.dto.OrderComboProductResponseDTO;
import com.Gyro.back_end_gyro.domain.order_combo_product.service.OrderComboProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-combo-products")
@RequiredArgsConstructor
public class OrderComboProductController {

    private final OrderComboProductService orderComboProductService;
    private final OrderService orderService;
    private final ComboService comboService;

    @PostMapping
    public ResponseEntity<OrderComboProductResponseDTO> create(@RequestBody OrderComboProductRequestDTO orderComboProductRequestDTO) {
        Order order = orderService.existsOrderById(orderComboProductRequestDTO.orderId());
        Combo combo = comboService.existsComboById(orderComboProductRequestDTO.comboId());

        return ResponseEntity.ok(orderComboProductService.create(order, combo));
    }
}
