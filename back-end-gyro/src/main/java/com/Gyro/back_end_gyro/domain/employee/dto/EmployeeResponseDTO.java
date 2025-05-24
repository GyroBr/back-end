package com.Gyro.back_end_gyro.domain.employee.dto;

import com.Gyro.back_end_gyro.domain.employee.entity.Employee;

public record EmployeeResponseDTO(

        Long employeeId,
        String name,
        String email,
        String password,
        Double totalRevenue,
        Integer totalSales

) {

    public EmployeeResponseDTO(Employee employee) {
        this(employee.getId(), employee.getName(), employee.getEmail(), employee.getPassword(), employee.getTotalRevenue(), employee.getTotalSales());
    }
}
