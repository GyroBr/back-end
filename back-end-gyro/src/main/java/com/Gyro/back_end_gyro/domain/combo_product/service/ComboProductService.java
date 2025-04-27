package com.Gyro.back_end_gyro.domain.combo_product.service;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.combo_product.dto.ComboProductRequestDTO;
import com.Gyro.back_end_gyro.domain.combo_product.dto.ComboProductResponseDTO;
import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import com.Gyro.back_end_gyro.domain.combo_product.repository.ComboProductRepository;
import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.ConflitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ComboProductService {

    private final ComboProductRepository comboProductRepository;


    public ComboProductResponseDTO create(Combo combo, Product product, Integer productQuantity) {

        isAvaibleTransaction(product, productQuantity);


        ComboProduct comboProduct = new ComboProduct(combo, product, productQuantity);

        combo.getComboProducts().add(comboProduct);

        combo.setRawPrice(BigDecimal.valueOf(combo.getComboProducts().stream()
                        .mapToDouble(ComboProduct::getRawPrice)
                        .sum())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());





        return new ComboProductResponseDTO(comboProductRepository.save(comboProduct));
    }


    public ComboProduct existById(Long id) {
        return comboProductRepository.findById(id).orElseThrow(() -> new ConflitException("Combo product not found"));
    }


    private Boolean isAvaibleTransaction(Product product, Integer productQuantityToCombo) {

        if (product.getQuantity() >= productQuantityToCombo) {
            return true;
        }

        throw new ConflitException("Not enough stock");
    }
}
