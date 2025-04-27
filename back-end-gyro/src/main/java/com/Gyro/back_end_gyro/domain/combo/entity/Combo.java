package com.Gyro.back_end_gyro.domain.combo.entity;

import com.Gyro.back_end_gyro.domain.combo.dto.ComboRequestDTO;
import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "table_combo")
@Table(name = "table_combo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comboName;
    private Double rawPrice;
    private Double priceByStore;

    @OneToMany(mappedBy = "combo", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ComboProduct> comboProducts= new ArrayList<>();

    @ManyToOne
    private Company company;


    public Combo(ComboRequestDTO dto) {
        this.comboName = dto.comboName();
        this.priceByStore = dto.priceByStore();
    }
}
