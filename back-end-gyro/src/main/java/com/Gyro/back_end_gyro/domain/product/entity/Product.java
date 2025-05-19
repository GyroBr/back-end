package com.Gyro.back_end_gyro.domain.product.entity;

import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import com.Gyro.back_end_gyro.domain.product.dto.ProductRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "table_product")
@Table(name = "table_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String volume;
    private Integer quantity;
    @Column(columnDefinition = "TEXT")
    private String image;
    private LocalDate expiresAt;
    private Integer warningQuantity;
    private String barCode;
    private Integer totalSales = 0;
    private Boolean isExpiredProduct;
    private Boolean isOutOfStock;
    private Boolean isSendedToEmail;
    private String category;


    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ComboProduct> comboProducts = new ArrayList<>();

    public Product(ProductRequestDTO productRequestDTO) {
        this.name = productRequestDTO.name();
        this.price = productRequestDTO.price();
        this.volume = productRequestDTO.volume();
        this.quantity = productRequestDTO.quantity();
        this.expiresAt = productRequestDTO.expiresAt();
        this.warningQuantity = productRequestDTO.warningQuantity();
        this.barCode = productRequestDTO.barCode();
        this.category = replateToUpper(productRequestDTO.category());
        this.isSendedToEmail = false;
    }

    private String replateToUpper(String category) {
        return category.substring(0, 1).toUpperCase() + category.substring(1);
    }

}
