package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.service.CompanyService;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.employee.service.EmployeeService;
import com.Gyro.back_end_gyro.domain.order.dto.OrderRequestDTO;
import com.Gyro.back_end_gyro.domain.order.dto.OrderResponseDTO;
import com.Gyro.back_end_gyro.domain.order.service.OrderService;
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

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints para gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;
    private final TokenService tokenService;
    private final EmployeeService employeeService;
    private final CompanyService companyService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar um novo pedido",
            description = "Cria um novo pedido associado a um funcionário. Requer autenticação via token JWT no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"employeeId\": 1, \"companyId\": 1, \"totalAmount\": 100.0, \"status\": \"PENDENTE\"}"
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
                    description = "Funcionário ou empresa não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Funcionário ou empresa não encontrado\"}"
                            )
                    )
            )
    })
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestHeader("Authorization") String tokenIssuer,
            @RequestBody @Valid OrderRequestDTO orderRequestDTO
    ) {
        Employee employee = employeeService.existsEmployeeById(tokenService.getEmployeeIdFromToken(tokenIssuer));
        return ResponseEntity.ok(orderService.createOrder(employee, orderRequestDTO));
    }

    @GetMapping("/get-all-orders")
    @Operation(
            summary = "Obter todos os pedidos de uma empresa",
            description = "Retorna uma lista de todos os pedidos associados à empresa do token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class, type = "array"),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"employeeId\": 1, \"companyId\": 1, \"totalAmount\": 100.0, \"status\": \"PENDENTE\"}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum pedido encontrado",
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
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        Company company = companyService.existsCompanyId(tokenService.getCompanyIdFromToken(tokenIssuer));
        List<OrderResponseDTO> allOrders = orderService.getAllOrdersByCompany(company);

        if (allOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allOrders);
    }
}