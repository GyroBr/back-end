package com.Gyro.back_end_gyro.externals.via.cep.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponseDTO(

        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        String estado
) {
}
