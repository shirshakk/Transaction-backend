package com.wallete.wallete_service.dao;

import java.time.Instant;

public record ErrorResponse (
    ErrorDetail error,
    Instant timestamp 
) {
    public record ErrorDetail(
            String code,
            String message,
            String requestId
    ) {}
    
}