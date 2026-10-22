package com.transaction.transaction_service.dao.request;

import java.math.BigDecimal;

public record WithdrawRequest(
    BigDecimal amount
) {
    
}
