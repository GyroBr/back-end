package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.user.dto.UserLoginRequestDTO;
import com.Gyro.back_end_gyro.domain.user.dto.UserResponseDTO;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.service.UserService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "Realiza o login de um usuário",
            description = "Recebe credenciais (e-mail e senha) e retorna um token JWT válido para autenticação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
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
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"E-mail ou senha incorretos\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginRequestDTO.email(), userLoginRequestDTO.password()
        );
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new UserResponseDTO(token));
    }

    @GetMapping("/is-admin")
    @Operation(
            summary = "Verifica se o usuário é um administrador",
            description = "Verifica se o usuário associado ao token JWT fornecido possui permissões de administrador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Verificação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(
                                    value = "true"
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
            )
    })
    public ResponseEntity<Boolean> isAdmin(@RequestHeader("Authorization") String tokenIssuer) {
        var role = tokenService.getUserRoleFromToken(tokenIssuer);
        return ResponseEntity.ok(userService.isAdmin(role));
    }
}