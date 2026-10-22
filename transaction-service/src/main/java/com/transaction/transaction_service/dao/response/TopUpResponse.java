package com.transaction.transaction_service.dao.response;

import java.util.UUID;

import com.transaction.transaction_service.enums.TransactionStatus;

public record TopUpResponse(
    UUID transactionId,
    TransactionStatus status,
    String failureReason) {}