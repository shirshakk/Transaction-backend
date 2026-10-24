package com.transaction.transaction_service.dao.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.transaction.transaction_service.enums.TransactionType;

public record TopUpEvent(
    UUID transactionId,
    UUID userId,
    BigDecimal amount,
    TransactionType transactionType
) {}