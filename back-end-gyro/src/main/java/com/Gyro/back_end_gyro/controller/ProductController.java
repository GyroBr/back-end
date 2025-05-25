package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.service.CompanyService;
import com.Gyro.back_end_gyro.domain.product.dto.ProductRequestDTO;
import com.Gyro.back_end_gyro.domain.product.dto.ProductResponseDTO;
import com.Gyro.back_end_gyro.domain.product.service.ProductService;
import com.Gyro.back_end_gyro.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints para gerenciamento de produtos")
public class ProductController {

    private final ProductService productService;
    private final TokenService tokenService;
    private final CompanyService companyService;

    @PostMapping("/register")
    @Operation(summary = "Registrar um novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ProductResponseDTO> create(
            @RequestHeader("Authorization") String token,
            @RequestPart("product") @Valid ProductRequestDTO request,
            @RequestPart("image") MultipartFile image) {

        Company company = getCompanyFromToken(token);
        ProductResponseDTO response = productService.createProduct(company, request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/top-products")
    @Operation(summary = "Obter os produtos mais vendidos")
    public ResponseEntity<List<ProductResponseDTO>> getTopSellingProducts(
            @RequestHeader("Authorization") String token) {

        Company company = getCompanyFromToken(token);
        List<ProductResponseDTO> products = productService.getTopSellingProducts(company.getId());
        return buildListResponse(products);
    }

    @GetMapping("/get-expired-products")
    @Operation(summary = "Obter produtos expirados")
    public ResponseEntity<List<ProductResponseDTO>> getExpiredProducts(
            @RequestHeader("Authorization") String token) {

        Company company = getCompanyFromToken(token);
        return buildListResponse(productService.getExpiredProducts(company.getId()));
    }

    @GetMapping("/get-out-of-stock-products")
    @Operation(summary = "Obter produtos fora de estoque")
    public ResponseEntity<List<ProductResponseDTO>> getOutOfStockProducts(
            @RequestHeader("Authorization") String token) {

        Company company = getCompanyFromToken(token);
        return buildListResponse(productService.getOutOfStockProducts(company.getId()));
    }

    @GetMapping("/get-all-by-company")
    @Operation(summary = "Obter todos os produtos da empresa")
    public ResponseEntity<List<ProductResponseDTO>> getAllByCompany(
            @RequestHeader("Authorization") String token) {

        Company company = getCompanyFromToken(token);
        return buildListResponse(productService.getAllProductsByCompany(company));
    }

    @GetMapping("/get-categories")
    @Operation(summary = "Obter categorias de produtos")
    public ResponseEntity<List<String>> getCategories(
            @RequestHeader("Authorization") String token) {

        Company company = getCompanyFromToken(token);
        return buildListResponse(productService.getProductCategories(company));
    }

    @PutMapping("/update-product/{id}")
    @Operation(summary = "Atualizar um produto")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid ProductRequestDTO request) {

        getCompanyFromToken(token);
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/delete-product/{id}")
    @Operation(summary = "Remover um produto")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        getCompanyFromToken(token);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private Company getCompanyFromToken(String token) {
        Long companyId = tokenService.getCompanyIdFromToken(token);
        return companyService.existsCompanyId(companyId);
    }

    private <T> ResponseEntity<List<T>> buildListResponse(List<T> list) {
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }
}