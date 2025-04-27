package com.Gyro.back_end_gyro.domain.address.entity;

import com.Gyro.back_end_gyro.domain.address.dto.AddressRequestDTO;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "table_address")
@Table(name = "table_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String postalCode;
    private String street;
    private String neighborhood;
    private String federativeUnity;
    private String city;
    private String number;

    @OneToOne(mappedBy = "address",cascade = CascadeType.ALL)
    private Company company;

    public Address(AddressRequestDTO requestDTO) {
        this.postalCode = requestDTO.postalCode();
        this.street = requestDTO.street();
        this.neighborhood = requestDTO.neighborhood();
        this.federativeUnity = requestDTO.federativeUnity();
        this.city = requestDTO.city();
        this.number = requestDTO.number();
    }
}
