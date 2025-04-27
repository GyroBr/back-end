package com.Gyro.back_end_gyro.domain.combo_product.entity;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.order_combo_product.entity.OrderComboProduct;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "table_combo_product")
@Table(name = "table_combo_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer productQuantity;

    private Double rawPrice;


    @ManyToOne
    private Combo combo;

    @ManyToOne
    private Product product;


    @OneToMany(mappedBy = "comboProduct", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderComboProduct> orderComboProducts = new ArrayList<>();

    public ComboProduct(Combo combo, Product product, Integer productQuantity) {
        this.combo = combo;
        this.product = product;
        this.productQuantity = productQuantity;
        this.rawPrice = product.getPrice() * productQuantity;
    }


}
