package com.ua.polishchuk.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ExceptionDto {

    private HttpStatus status;
    private String errorMessage;
    private LocalDateTime timestamp;

    public ExceptionDto(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.status = httpStatus;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
