package com.user.user_service.dao;

import java.time.Instant;

public record ApiError(
    ErrorDetail error,
    Instant timestamp 
) {
    public record ErrorDetail(
            String code,
            String message,
            String requestId
    ) {}
    
}

