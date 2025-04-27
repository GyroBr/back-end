package com.Gyro.back_end_gyro.domain.user.dto;

import com.Gyro.back_end_gyro.domain.user.enums.Roles;

public record UserRequestDTO(

        String name,

        String email,

        String password,

        Roles role
) {
}
