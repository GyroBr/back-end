package com.Gyro.back_end_gyro.domain.employee.entity;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeRequestDTO;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "table_employee")
@Table(name = "table_employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private Integer totalSales;
    private Double totalRevenue;

    @OneToOne
    private User user;


    @ManyToOne
    private Company company;


    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Employee(EmployeeRequestDTO requestDTO) {
        this.name = requestDTO.name();
        this.email = requestDTO.email();
        this.password = requestDTO.password();
        this.totalSales = 0;
        this.totalRevenue = 0.0;
    }
}
