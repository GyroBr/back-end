package com.Gyro.back_end_gyro.domain.company.dto;

import com.Gyro.back_end_gyro.domain.address.dto.AddressResponseDTO;
import com.Gyro.back_end_gyro.domain.combo.dto.ComboResponseDTO;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeResponseDTO;
import com.Gyro.back_end_gyro.domain.order.dto.OrderResponseDTO;
import com.Gyro.back_end_gyro.domain.product.dto.ProductResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public record CompanyResponseDTO(

        Long companyId,
        String name,
        String email,
        String password,
        String sector,
        String cnpj,
        String phoneNumber,
        List<EmployeeResponseDTO> employees,
        AddressResponseDTO address,
        List<ProductResponseDTO> products,
        List<OrderResponseDTO> orders,
        List<ComboResponseDTO> combos,
        Double totalValueOfSales

) {

    public CompanyResponseDTO(Company company) {
        this(
                company.getId(),
                company.getName(),
                company.getEmail(),
                company.getPassword(),
                company.getSector().name(),
                company.getCnpj(),
                company.getPhoneNumber(),
                company.getEmployees().stream().map(EmployeeResponseDTO::new).collect(Collectors.toList()),
                new AddressResponseDTO(company.getAddress()),
                company.getProducts().stream().map(ProductResponseDTO::new).collect(Collectors.toList()),
                company.getOrders().stream().map(OrderResponseDTO::new).collect(Collectors.toList()),
                company.getCombos().stream().map(ComboResponseDTO::new).collect(Collectors.toList()),
                company.getTotalRevenuOfSales()
        );
    }
}
