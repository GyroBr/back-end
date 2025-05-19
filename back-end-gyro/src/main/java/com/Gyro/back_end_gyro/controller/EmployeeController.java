package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.service.CompanyService;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeRequestDTO;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeResponseDTO;
import com.Gyro.back_end_gyro.domain.employee.service.EmployeeService;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Endpoints para gerenciamento de funcionários")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TokenService tokenService;
    private final CompanyService companyService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar um novo funcionário",
            description = "Cria um novo funcionário associado a uma empresa. Requer autenticação via token JWT no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Funcionário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"João Silva\", \"email\": \"joao.silva@empresa.com\", \"role\": \"VENDEDOR\"}"
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
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @RequestHeader("Authorization") String tokenIssuer,
            @RequestBody @Valid EmployeeRequestDTO employeeRequestDTO
    ) {
        Company company = companyService.existsCompanyId(tokenService.getCompanyIdFromToken(tokenIssuer));
        return ResponseEntity.ok(employeeService.createEmployee(company, employeeRequestDTO));
    }

    @GetMapping
    @Operation(
            summary = "Obter informações do funcionário",
            description = "Retorna as informações do funcionário associado ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações do funcionário retornadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"João Silva\", \"email\": \"joao.silva@empresa.com\", \"role\": \"VENDEDOR\"}"
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
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Funcionário não encontrado\"}"
                            )
                    )
            )
    })
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByToken(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        return ResponseEntity.ok(employeeService.getEmployeeById(tokenService.getEmployeeIdFromToken(tokenIssuer)));
    }

    @GetMapping("/top-employees")
    @Operation(
            summary = "Obter os top 10 funcionários",
            description = "Retorna uma lista dos 10 funcionários com melhor desempenho na empresa associada ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de funcionários retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class, type = "array"),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"name\": \"João Silva\", \"email\": \"joao.silva@empresa.com\", \"role\": \"VENDEDOR\"}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum funcionário encontrado",
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
    public ResponseEntity<List<EmployeeResponseDTO>> getTop10Employee(
            @RequestHeader("Authorization") String tokenIssuer
    ) {
        Company company = companyService.existsCompanyId(tokenService.getCompanyIdFromToken(tokenIssuer));
        List<EmployeeResponseDTO> employees = employeeService.topTenSallerEmployee(company);

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(employees);
    }

    @PutMapping
    @Operation(
            summary = "Atualizar funcionário",
            description = "Atualiza os dados do funcionário associado ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Funcionário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"name\": \"João Silva\", \"email\": \"joao.silva@empresa.com\", \"role\": \"ADMIN\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Requisição inválida\"}"
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
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Funcionário não encontrado\"}"
                            )
                    )
            )
    })
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @RequestHeader("Authorization") String tokenIssuer,
            @RequestBody @Valid EmployeeRequestDTO employeeRequestDTO
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(tokenService.getEmployeeIdFromToken(tokenIssuer), employeeRequestDTO));
    }

    @DeleteMapping
    @Operation(
            summary = "Deletar funcionário",
            description = "Remove o funcionário associado ao token JWT fornecido no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Funcionário deletado com sucesso",
                    content = @Content
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
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Funcionário não encontrado\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> deleteEmployee(@RequestHeader("Authorization") String tokenIssuer) {
        employeeService.deleteEmployee(tokenService.getEmployeeIdFromToken(tokenIssuer));
        return ResponseEntity.noContent().build();
    }

}