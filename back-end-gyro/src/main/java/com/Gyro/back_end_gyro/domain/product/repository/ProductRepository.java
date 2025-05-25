package com.Gyro.back_end_gyro.domain.product.repository;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM table_product p 
        JOIN p.orderProducts op 
        WHERE p.company.id = :companyId
        GROUP BY p 
        ORDER BY COUNT(op) DESC
    """)
    List<Product> findTopSellingProducts(Long companyId);

    Optional<Product> findByIdAndCompany(Long id, Company company);

    List<Product> findByCompanyIdAndIsExpiredProductTrue(Long companyId);
    List<Product> findByCompanyIdAndIsExpiredProductFalse(Long companyId);
    List<Product> findByIsExpiredProductTrueAndIsSendedToEmailFalse();
    List<Product> findByExpiresAtBeforeAndIsExpiredProductFalse(LocalDate date);

    List<Product> findByCompanyIdAndIsOutOfStockTrue(Long companyId);
    List<Product> findByCompanyIdAndIsOutOfStockFalse(Long companyId);
    List<Product> findByIsOutOfStockTrueAndIsSendedToEmailFalse();
    List<Product> findByQuantityLessThanEqualAndIsOutOfStockFalse(Integer warningQuantity);

    List<Product> findByIsSendedToEmailTrue();
    List<Product> findByIsSendedToEmailFalse();

    List<Product> findByCompanyIdAndIsExpiredProductTrueAndIsSendedToEmailFalse(Long companyId);
    List<Product> findByCompanyIdAndIsOutOfStockTrueAndIsSendedToEmailFalse(Long companyId);
}