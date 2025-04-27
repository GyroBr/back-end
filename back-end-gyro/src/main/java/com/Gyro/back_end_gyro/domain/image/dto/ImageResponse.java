package com.Gyro.back_end_gyro.domain.image.dto;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record ImageResponse(
        Resource resource,
        MediaType contentType
) {
}
