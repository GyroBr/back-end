package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.service.CompanyService;
import com.Gyro.back_end_gyro.domain.product.dto.ProductRequestDTO;
import com.Gyro.back_end_gyro.domain.product.dto.ProductResponseDTO;
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
    @Operation(
            summary = "Registrar um novo produto",
            description = "Cria um novo produto associado a uma empresa. Requer autenticação via token JWT no cabeçalho e o envio de uma imagem."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"Produto Exemplo\", \"description\": \"Descrição do produto\", \"price\": 100.0, \"quantity\": 10}"
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
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestHeader("Authorization") String tokenIssuer,
            @RequestPart("product") @Valid ProductRequestDTO productRequestDTO,
            @RequestPart("image") MultipartFile imageFile
    ) {
        Long companyId = tokenService.getCompanyIdFromToken(tokenIssuer);
        return ResponseEntity.ok(productService.createProduct(companyService.existsCompanyId(companyId), productRequestDTO, imageFile));
    }

    @GetMapping("/top-products")
    @Operation(
            summary = "Obter os produtos mais vendidos",
            description = "Retorna uma lista dos 10 produtos mais vendidos da empresa associada ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos mais vendidos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class, type = "array"),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"name\": \"Produto Exemplo\", \"description\": \"Descrição do produto\", \"price\": 100.0, \"quantity\": 10}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{}"
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
    public ResponseEntity<List<ProductResponseDTO>> getMostSallers(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        Long companyId = tokenService.getCompanyIdFromToken(tokenIssuer);
        List<ProductResponseDTO> mostSaller = productService.getTop10MostSellingProducts(companyId);

        if (mostSaller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mostSaller);
    }

    @GetMapping("/get-expired-products")
    @Operation(
            summary = "Obter produtos expirados",
            description = "Retorna uma lista de produtos expirados da empresa associada ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos expirados retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class, type = "array"),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"name\": \"Produto Exemplo\", \"description\": \"Descrição do produto\", \"price\": 100.0, \"quantity\": 10}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum produto expirado encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{}"
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
    public ResponseEntity<List<ProductResponseDTO>> getExpiredProducts(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        Long companyId = tokenService.getCompanyIdFromToken(tokenIssuer);
        List<ProductResponseDTO> expiredProducts = productService.getExiperedProducts(companyId);

        if (expiredProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(expiredProducts);
    }

    @GetMapping("/get-out-of-stock-products")
    @Operation(
            summary = "Obter produtos fora de estoque",
            description = "Retorna uma lista de produtos fora de estoque da empresa associada ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos fora de estoque retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class, type = "array"),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"name\": \"Produto Exemplo\", \"description\": \"Descrição do produto\", \"price\": 100.0, \"quantity\": 0}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum produto fora de estoque encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{}"
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
    public ResponseEntity<List<ProductResponseDTO>> getOutOfStockProducts(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        Long companyId = tokenService.getCompanyIdFromToken(tokenIssuer);
        List<ProductResponseDTO> outOfStockProducts = productService.getOutOfStockProducts(companyId);

        if (outOfStockProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(outOfStockProducts);
    }


    @GetMapping("/get-all-by-company")
    @Operation(
            summary = "Obter todos os produtos de uma empresa",
            description = "Retorna uma lista de todos os produtos associados à empresa do token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class, type = "array"),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"name\": \"Produto Exemplo\", \"description\": \"Descrição do produto\", \"price\": 100.0, \"quantity\": 10}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{}"
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
    public ResponseEntity<List<ProductResponseDTO>> getAllByCompanyId(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        var companyId = tokenService.getCompanyIdFromToken(tokenIssuer);
        var company = companyService.existsCompanyId(companyId);
        var products = productService.getAllProductsByCompanyId(company);

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/get-categories")
    @Operation(
            summary = "Obtém todas as categorias de produtos de uma empresa",
            description = "Retorna uma lista de categorias de produtos associadas à empresa do usuário autenticado. " +
                    "O token JWT no cabeçalho 'Authorization' é usado para identificar a empresa."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categorias retornadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class, type = "array", example = "[\"Eletrônicos\", \"Roupas\", \"Alimentos\"]")
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhuma categoria encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "[]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido ou não fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Token inválido ou não fornecido\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Acesso negado\"}"
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
    public ResponseEntity<List<String>> getCategories(@RequestHeader("Authorization") String tokenIssuer) {
        Company company = companyService.existsCompanyId(tokenService.getCompanyIdFromToken(tokenIssuer));

        List<String> categories = productService.getAllProductCategories(company);

        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categories);
    }
}

