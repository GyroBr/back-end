package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.combo.dto.ComboRequestDTO;
import com.Gyro.back_end_gyro.domain.combo.dto.ComboResponseDTO;
import com.Gyro.back_end_gyro.domain.combo.service.ComboService;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.service.CompanyService;
import com.Gyro.back_end_gyro.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/combos")
@RequiredArgsConstructor
@Tag(name = "Combos", description = "Endpoints para gerenciamento de combos")
public class ComboController {

    private final ComboService comboService;
    private final TokenService tokenService;
    private final CompanyService companyService;

    @PostMapping
    @Operation(
            summary = "Registrar um novo combo",
            description = "Cria um novo combo associado a uma empresa. Requer autenticação via token JWT no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Combo registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComboResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"Combo Promocional\", \"description\": \"Combo com desconto especial\", \"price\": 50.0}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Dados inválidos fornecidos\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Token JWT inválido ou ausente\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Empresa não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Empresa não encontrada\"}"
                            )
                    )
            )
    })
    public ResponseEntity<ComboResponseDTO> registerCombo(
            @RequestHeader("Authorization") String tokenIssuer,
            @RequestBody @Valid ComboRequestDTO comboRequestDTO
    ) {
        Company company = companyService.existsCompanyId(tokenService.getCompanyIdFromToken(tokenIssuer));
        return ResponseEntity.ok(comboService.registerCombo(company, comboRequestDTO));
    }
}