package com.Gyro.back_end_gyro.domain.company.dto;

import com.Gyro.back_end_gyro.domain.address.dto.AddressRequestDTO;
import com.Gyro.back_end_gyro.domain.company.enums.Sector;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompanyRequestDTO(

        @NotBlank
        String name,

        @NotBlank
        @Size(min = 11, max = 11)
        String phoneNumber,

        @NotBlank
        @Size(min = 14, max = 14)
        String cnpj,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6)
        String password,

        @NotNull
        Sector sector,

        @NotNull
        @Valid
        AddressRequestDTO address


) {
}
