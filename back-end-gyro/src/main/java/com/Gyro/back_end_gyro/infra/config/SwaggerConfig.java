package com.Gyro.back_end_gyro.infra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gyro API",
                version = "v1",
                description = "Documentação da API Gyro"
        )
)
public class SwaggerConfig {
}
