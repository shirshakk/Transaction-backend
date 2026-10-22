package com.transaction.transaction_service.Exception;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.transaction.transaction_service.dao.ErrorResponse;
import com.transaction.transaction_service.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

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

   
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(
            CustomException ex, HttpServletRequest request) {

        ErrorCode code = ex.getErrorCode();
        return buildError(code.getStatus(), code.name(), code.getMessage(), request);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");

        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", message, request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex, HttpServletRequest request) {

        // log the real cause on the server, but return nothing internal to the client
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Something went wrong", request);
    }
}
