package com.Gyro.back_end_gyro.domain.address.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(

        @NotBlank
        String postalCode,

        @NotBlank
        String street,

        @NotBlank
        String neighborhood,

        @NotBlank
        String federativeUnity,

        @NotBlank
        String city,

        @NotBlank
        String number

) {
}
