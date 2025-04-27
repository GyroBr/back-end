package com.Gyro.back_end_gyro.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
