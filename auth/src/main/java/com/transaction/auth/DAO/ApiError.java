package com.transaction.auth.DAO;

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
