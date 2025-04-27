package com.Gyro.back_end_gyro.domain.order.dto;

import com.Gyro.back_end_gyro.domain.combo.dto.ComboResponseDTO;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeResponseDTO;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order.enums.PaymentMethod;
import com.Gyro.back_end_gyro.domain.order_combo_product.dto.OrderComboProductResponseDTO;
import com.Gyro.back_end_gyro.domain.order_product.dto.OrderProductResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record OrderResponseDTO(

        Long orderId,
        PaymentMethod paymentMethod,
        Double purchaseTotal,
        Double change,
        Double cashForPayment,
        Integer productQuantity,
        Boolean haveAChange,
        EmployeeResponseDTO employee,
        LocalDateTime createdAt,
        List<OrderProductResponseDTO> itemsOfProducts,
        List<OrderComboProductResponseDTO> itemsOfCombos

) {
    public OrderResponseDTO(Order order) {
        this(
                order.getId(),
                order.getPaymentMethod(),
                order.getPurchaseTotal(),
                order.getChange(),
                order.getCashForPayment(),
                order.getProductQuantity(),
                order.getHaveAChange(),
                new EmployeeResponseDTO(order.getEmployee()),
                order.getCreatedAt(),
                order.getOrderProducts().stream()
                        .map(OrderProductResponseDTO::new)
                        .collect(Collectors.toList()),
                order.getOrderComboProducts().stream().map(OrderComboProductResponseDTO::new)
                        .collect(Collectors.toList())

        );

    }
}
