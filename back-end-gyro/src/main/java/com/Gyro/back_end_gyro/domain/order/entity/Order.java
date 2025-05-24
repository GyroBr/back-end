package com.Gyro.back_end_gyro.domain.order.entity;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.order.dto.OrderRequestDTO;
import com.Gyro.back_end_gyro.domain.order.enums.PaymentMethod;
import com.Gyro.back_end_gyro.domain.order_combo_product.entity.OrderComboProduct;
import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "table_order")
@Table(name = "table_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Double purchaseTotal;

    private Boolean haveAChange;

    private Double change;

    private Double cashForPayment;

    private LocalDateTime createdAt;

    private Integer productQuantity = 0;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderComboProduct> orderComboProducts = new ArrayList<>();


    public Order(Employee employee, OrderRequestDTO requestDTO) {
        this.employee = employee;
        this.company = employee.getCompany();
        this.createdAt = LocalDateTime.now();

        if (cashForPayment == null) {
            this.cashForPayment = 0.0;
        }
        this.cashForPayment = requestDTO.cashForPayment();
        this.purchaseTotal = 0.0;
        this.change = 0.0;
    }


}
