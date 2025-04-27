package com.Gyro.back_end_gyro.domain.order_combo_product.repository;

import com.Gyro.back_end_gyro.domain.order_combo_product.entity.OrderComboProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderComboProductRepository extends JpaRepository<OrderComboProduct, Long> {
}
