package com.Gyro.back_end_gyro.domain.employee.repository;

import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM table_employee e WHERE e.company.id = :companyId AND e.totalSales IS NOT NULL ORDER BY e.totalSales DESC")
    List<Employee> findTop10ByTotalSales(Long companyId);
}
