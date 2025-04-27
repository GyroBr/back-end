package com.Gyro.back_end_gyro.domain.combo.dto;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.combo_product.dto.ComboProductResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public record ComboResponseDTO(
        Long id,
        String comboName,
        Double rawPrice,
        Double priceByStore,
        List<ComboProductResponseDTO> comboProducts
) {

    public ComboResponseDTO(Combo combo) {
        this(
                combo.getId(),
                combo.getComboName(),
                combo.getRawPrice(),
                combo.getPriceByStore(),
                combo.getComboProducts().stream()
                        .map(ComboProductResponseDTO::new)
                        .collect(Collectors.toList()));
    }
}
