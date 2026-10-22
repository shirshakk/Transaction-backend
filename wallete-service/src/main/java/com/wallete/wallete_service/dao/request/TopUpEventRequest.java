package com.wallete.wallete_service.dao.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.wallete.wallete_service.enums.TransactionType;




public record TopUpEventRequest(
    UUID transactionId,
    UUID userId,
    BigDecimal amount,
    TransactionType transactionType
) {}