package com.wallete.wallete_service.exception;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wallete.wallete_service.dao.ErrorResponse;
import com.wallete.wallete_service.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice 
public class GlobalException {


    public ResponseEntity<ErrorResponse> buildError(
        HttpStatus status,
        String code,
        String message,
        HttpServletRequest request
    ){
         String requestId = request.getHeader("X-Request-ID");

        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        code, message, requestId
                ),
                Instant.now()
        );

        return ResponseEntity.status(status).body(response);
    }

     @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        if (ex.getCause() instanceof InvalidFormatException ife
                && ife.getTargetType() != null
                && ife.getTargetType().isEnum()) {

            String allowed = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            return buildError(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_ENUM_VALUE",
                    "Invalid value '" + ife.getValue() + "'. Allowed values: " + allowed,
                    request);
        }

        return buildError(
                HttpStatus.BAD_REQUEST,
                "MALFORMED_REQUEST",
                "Request body is missing or malformed",
                request);
    }

    // Field is null/missing, caught by @Valid + @NotNull
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", message, request);
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(
            CustomException ex, HttpServletRequest request) {

        ErrorCode code = ex.getErrorCode();
        return buildError(code.getStatus(), code.name(), code.getMessage(), request);
    }

  

}
