package com.Gyro.back_end_gyro.externals.via.cep.service;

import com.Gyro.back_end_gyro.externals.via.cep.dto.ViaCepResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ViaCepApiService {

    private final RestTemplate restTemplate;

    public ViaCepResponseDTO fethcViaCep(String cep) {
        String cepReturn = "https://viacep.com.br/ws/%s/json/".formatted(cep);
        return restTemplate.getForObject(cepReturn, ViaCepResponseDTO.class);
    }
}
