package com.Gyro.back_end_gyro.domain.combo_product.repository;

import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboProductRepository extends JpaRepository<ComboProduct, Long> {
}
