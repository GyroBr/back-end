package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.company.dto.CompanyRequestDTO;
import com.Gyro.back_end_gyro.domain.company.dto.CompanyResponseDTO;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Endpoints para gerenciamento de empresas")
public class CompanyController {

    private final CompanyService companyService;
    private final TokenService tokenService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar uma nova empresa",
            description = "Cria uma nova empresa com os dados fornecidos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Empresa registrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CompanyResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"Empresa Exemplo\", \"cnpj\": \"12345678901234\", \"email\": \"empresa@exemplo.com\"}"
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
            )
    })
    public ResponseEntity<CompanyResponseDTO> createCompany(@RequestBody @Valid CompanyRequestDTO companyRequestDTO) {
        return ResponseEntity.ok(companyService.createCompany(companyRequestDTO));
    }

    @GetMapping("/get-company-info")
    @Operation(
            summary = "Obter informações de uma empresa",
            description = "Retorna as informações da empresa associada ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações da empresa retornadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CompanyResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"Empresa Exemplo\", \"cnpj\": \"12345678901234\", \"email\": \"empresa@exemplo.com\"}"
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
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@RequestHeader("Authorization") String tokenIssuer) {
        return ResponseEntity.ok(companyService.getCompanyById(tokenService.getCompanyIdFromToken(tokenIssuer)));
    }

    @PutMapping
    @Operation(
            summary = "Atualizar dados da empresa",
            description = "Atualiza os dados da empresa associada ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Empresa atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CompanyResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"Nova Empresa\", \"cnpj\": \"12345678901234\", \"email\": \"novoemail@empresa.com\"}"
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
    public ResponseEntity<CompanyResponseDTO> updateCompany(@RequestHeader("Authorization") String tokenIssuer, @RequestBody @Valid CompanyRequestDTO companyRequestDTO) {
        var companyId = tokenService.getCompanyIdFromToken(tokenIssuer);
        return ResponseEntity.ok(companyService.updateCompany(companyId, companyRequestDTO));
    }
}