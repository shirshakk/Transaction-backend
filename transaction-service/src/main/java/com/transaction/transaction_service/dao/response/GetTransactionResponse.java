package com.transaction.transaction_service.dao.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.transaction.transaction_service.enums.TransactionStatus;
import com.transaction.transaction_service.enums.TransactionType;

public record GetTransactionResponse(
    BigDecimal amount,
    UUID fromWalletId,
    UUID toWalletId,
    TransactionStatus status,
    TransactionType type,
    UUID TransactionId

) {
    
}
