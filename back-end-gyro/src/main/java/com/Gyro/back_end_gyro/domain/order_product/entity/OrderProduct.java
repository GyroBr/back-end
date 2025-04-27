package com.Gyro.back_end_gyro.domain.order_product.entity;

import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "table_order_product")
@Table(name = "table_order_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    private Integer quantity;

    private Double priceAtPurchase;



}
