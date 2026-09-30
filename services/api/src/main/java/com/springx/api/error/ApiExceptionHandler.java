package com.springx.api.error;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ProviderUnavailableException.class)
    public ResponseEntity<ApiError> providerUnavailable(ProviderUnavailableException exception) {
        return response(HttpStatus.SERVICE_UNAVAILABLE, "PROVIDER_NOT_CONFIGURED", exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> invalidRequest(Exception exception) {
        return response(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "The request is invalid.");
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status)
            .body(new ApiError(new ApiError.ErrorBody(code, message, Map.of())));
    }
}