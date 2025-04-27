package com.Gyro.back_end_gyro.domain.order.dto;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.order.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(


        @NotNull
        PaymentMethod paymentMethod,

        Double cashForPayment
) {
}
