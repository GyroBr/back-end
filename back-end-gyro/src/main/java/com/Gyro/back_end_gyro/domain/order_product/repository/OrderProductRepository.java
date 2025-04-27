package com.Gyro.back_end_gyro.domain.order_product.repository;

import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository  extends JpaRepository<OrderProduct, Long> {
}
