package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order_product.dto.OrderProductRequestDTO;
import com.Gyro.back_end_gyro.domain.order_product.dto.OrderProductResponseDTO;
import com.Gyro.back_end_gyro.domain.order_product.service.OrderProductService;
import com.Gyro.back_end_gyro.domain.order.service.OrderService;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.service.ProductService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-products")
@RequiredArgsConstructor
@Tag(name = "Order Products", description = "Endpoints para gerenciamento de produtos em pedidos")
public class OrderProductController {

    private final OrderProductService orderProductService;
    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("/register")
    @Operation(
            summary = "Adicionar um produto a um pedido",
            description = "Associa um produto a um pedido existente, com uma quantidade específica."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto adicionado ao pedido com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderProductResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"orderId\": 1, \"productId\": 1, \"orderQuantity\": 2}"
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
                    description = "Pedido ou produto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Pedido ou produto não encontrado\"}"
                            )
                    )
            )
    })
    public ResponseEntity<OrderProductResponseDTO> createOrderProduct(
            @RequestBody @Valid OrderProductRequestDTO orderProductRequestDTO
    ) {

        return ResponseEntity.ok(orderProductService.create(orderProductRequestDTO.orderId(), orderProductRequestDTO.productId(), orderProductRequestDTO.orderQuantity()));
    }
}