package com.transaction.transaction_service.dao.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TopUpEvent(
    UUID transactionId,
    UUID userId,
    BigDecimal amount
) {}