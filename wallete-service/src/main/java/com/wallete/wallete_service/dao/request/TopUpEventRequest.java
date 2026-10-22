package com.wallete.wallete_service.dao.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TopUpEventRequest(
    UUID transactionId,
    UUID userId,
    BigDecimal amount
) {}