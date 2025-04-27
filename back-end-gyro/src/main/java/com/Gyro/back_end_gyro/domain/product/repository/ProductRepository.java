package com.Gyro.back_end_gyro.domain.product.repository;

import com.Gyro.back_end_gyro.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM table_product p 
        JOIN p.orderProducts op 
        WHERE p.company.id = :companyId
        GROUP BY p 
        ORDER BY COUNT(op) DESC
    """)
    List<Product> findTopSellingProducts(Long companyId);

    List<Product> findByCompanyIdAndIsExpiredProductTrue(Long companyId);

    List<Product>findByCompanyIdAndIsOutOfStockTrue(Long companyId);
}

