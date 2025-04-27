package com.Gyro.back_end_gyro.domain.company.repository;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
}
