package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.externals.via.cep.dto.ViaCepResponseDTO;
import com.Gyro.back_end_gyro.externals.via.cep.service.ViaCepApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/externals")
@RequiredArgsConstructor
@Tag(name = "Externals", description = "Endpoints para integração com serviços externos")
public class ExternalsController {

    private final ViaCepApiService viaCepApiService;

    @GetMapping("/{cep}")
    @Operation(
            summary = "Consultar informações de um CEP",
            description = "Retorna as informações de endereço associadas a um CEP, utilizando a API ViaCEP."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações do CEP retornadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViaCepResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"cep\": \"01001-000\", \"logradouro\": \"Praça da Sé\", \"complemento\": \"lado ímpar\", \"bairro\": \"Sé\", \"localidade\": \"São Paulo\", \"uf\": \"SP\", \"ibge\": \"3550308\", \"gia\": \"1004\", \"ddd\": \"11\", \"siafi\": \"7107\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "CEP inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"CEP inválido\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CEP não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"CEP não encontrado\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro ao consultar o serviço externo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Erro ao consultar o serviço ViaCEP\"}"
                            )
                    )
            )
    })
    public ResponseEntity<ViaCepResponseDTO> fetchTeste(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepApiService.fethcViaCep(cep));
    }
}