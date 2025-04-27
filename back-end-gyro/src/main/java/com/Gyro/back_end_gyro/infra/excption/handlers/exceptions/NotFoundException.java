package com.Gyro.back_end_gyro.infra.excption.handlers.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
