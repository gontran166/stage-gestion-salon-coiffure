package com.gestionSalon.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private Map<String, String> errors;
}