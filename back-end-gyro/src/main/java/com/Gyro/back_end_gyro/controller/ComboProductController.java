package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.combo.service.ComboService;
import com.Gyro.back_end_gyro.domain.combo_product.dto.ComboProductRequestDTO;
import com.Gyro.back_end_gyro.domain.combo_product.dto.ComboProductResponseDTO;
import com.Gyro.back_end_gyro.domain.combo_product.service.ComboProductService;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.service.CompanyService;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.service.ProductService;
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
@RequestMapping("/combo-products")
@RequiredArgsConstructor
@Tag(name = "Combo Products", description = "Endpoints para gerenciamento de produtos em combos")
public class ComboProductController {

    private final ComboProductService comboProductService;
    private final ProductService productService;
    private final ComboService comboService;
    private final CompanyService companyService;
    private final TokenService tokenService;

    @PostMapping
    @Operation(
            summary = "Adicionar um produto a um combo",
            description = "Associa um produto existente a um combo, com uma quantidade específica."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto adicionado ao combo com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComboProductResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"comboId\": 1, \"productId\": 1, \"productQuantity\": 2}"
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
                    responseCode = "404",
                    description = "Produto ou combo não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Produto ou combo não encontrado\"}"
                            )
                    )
            )
    })
    public ResponseEntity<ComboProductResponseDTO> create(
            @RequestBody @Valid ComboProductRequestDTO requestDTO, @RequestHeader("Authorization") String token
    ) {
        Company company = companyService.existsCompanyId(tokenService.getCompanyIdFromToken(token));
        Product product = productService.existsProductAndCompany(requestDTO.productId(), company);
        Combo combo = comboService.existsComboById(requestDTO.comboId());

        return ResponseEntity.ok(comboProductService.create(combo, product, requestDTO.productQuantity()));
    }
}