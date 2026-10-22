package com.wallete.wallete_service.dao.response;

import java.math.BigDecimal;

import com.wallete.wallete_service.enums.WalletStatus;

import lombok.Data;
@Data 
public class WalletStatusResponse {
    BigDecimal balance;
    WalletStatus walletStatus;
    String currency;
}
