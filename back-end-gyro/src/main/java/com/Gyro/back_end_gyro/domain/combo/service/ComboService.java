package com.Gyro.back_end_gyro.domain.combo.service;

import com.Gyro.back_end_gyro.domain.combo.dto.ComboRequestDTO;
import com.Gyro.back_end_gyro.domain.combo.dto.ComboResponseDTO;
import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.combo.repository.ComboRepository;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComboService {

    private final ComboRepository comboRepository;


    public ComboResponseDTO registerCombo(Company company, ComboRequestDTO comboRequestDTO) {

        Combo combo = new Combo(comboRequestDTO);
        combo.setCompany(company);
        return new ComboResponseDTO(comboRepository.save(combo));
    }


    public Combo existsComboById(Long comboId) {
        return comboRepository.findById(comboId).orElseThrow(() -> new NotFoundException("combo not found"));
    }

}
