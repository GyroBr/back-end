package com.Gyro.back_end_gyro.domain.combo.repository;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboRepository extends JpaRepository<Combo, Long> {
}
