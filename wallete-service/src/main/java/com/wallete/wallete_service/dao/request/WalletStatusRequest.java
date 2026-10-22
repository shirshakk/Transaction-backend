package com.wallete.wallete_service.dao.request;

import com.wallete.wallete_service.enums.WalletStatus;

import jakarta.validation.constraints.NotNull;

public record WalletStatusRequest(
    @NotNull(message = "Wallet Status is required")
    WalletStatus walletStatus
) {
    
}
