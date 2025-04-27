package com.Gyro.back_end_gyro.domain.order_combo_product.entity;

import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "table_order_combo_product")
@Table(name = "table_order_combo_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderComboProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private ComboProduct comboProduct;


    public OrderComboProduct(Order order, ComboProduct comboProduct) {
        this.order = order;
        this.comboProduct = comboProduct;
    }
}
