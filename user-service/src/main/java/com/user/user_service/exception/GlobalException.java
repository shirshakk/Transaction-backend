package com.user.user_service.exception;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.user_service.dao.ApiError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice 
public class GlobalException {

    public ResponseEntity<ApiError> buildError(
        HttpStatus status,
        String code,
        String message,
        HttpServletRequest request
    ){
         String requestId = request.getHeader("X-Request-ID");

        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        ApiError response = new ApiError(
                new ApiError.ErrorDetail(
                        code, message, requestId
                ),
                Instant.now()
        );

        return ResponseEntity.status(status).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        if ("UserNotFoundException".equals(ex.getClass().getSimpleName())) {
            return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
        }
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR  ", ex.getMessage(), request);
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), request);
    }
}
