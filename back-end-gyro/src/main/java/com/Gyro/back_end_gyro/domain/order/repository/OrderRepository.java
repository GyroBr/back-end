package com.Gyro.back_end_gyro.domain.order.repository;

import com.Gyro.back_end_gyro.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order>findAllByCompanyId(Long companyId);
}
