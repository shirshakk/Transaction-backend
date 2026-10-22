package com.wallete.wallete_service.dao.response;

import java.util.UUID;

import com.wallete.wallete_service.enums.TransactionStatus;

public record TopUpResponse(
    UUID transactionId,
    TransactionStatus status,
    String failureReason) {}