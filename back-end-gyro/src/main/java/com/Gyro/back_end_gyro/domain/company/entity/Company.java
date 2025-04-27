package com.Gyro.back_end_gyro.domain.company.entity;

import com.Gyro.back_end_gyro.domain.address.entity.Address;
import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.company.dto.CompanyRequestDTO;
import com.Gyro.back_end_gyro.domain.company.enums.Sector;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "table_company")
@Table(name = "table_company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private String cnpj;
    private String email;
    private String password;
    private Double totalRevenuOfSales = 0.0;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Combo> combos = new ArrayList<>();



    public Company(CompanyRequestDTO companyRequestDTO) {
        this.name = companyRequestDTO.name();
        this.phoneNumber = companyRequestDTO.phoneNumber();
        this.cnpj = companyRequestDTO.cnpj();
        this.email = companyRequestDTO.email();
        this.password = companyRequestDTO.password();
        this.sector = companyRequestDTO.sector();

    }
}
