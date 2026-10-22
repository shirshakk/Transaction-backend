package com.wallete.wallete_service.dao.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferEventRequest(
    UUID transactionId,
    UUID senderId,
    UUID receverId,
    BigDecimal amount
) {
} 