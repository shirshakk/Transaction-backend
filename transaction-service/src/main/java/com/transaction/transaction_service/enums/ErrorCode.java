package com.transaction.transaction_service.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter 
@AllArgsConstructor 
public enum ErrorCode {
    INVALID_AMOUNT("Amount must be greater than zero", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("Insufficient balance", HttpStatus.UNPROCESSABLE_CONTENT),
    WALLET_NOT_FOUND("Wallet not found", HttpStatus.NOT_FOUND),
    WALLET_UNAVAILABLE("Wallet is inactive or unavailable", HttpStatus.UNPROCESSABLE_CONTENT),
    INVALID_SENDER_ID("Sender ID not Fount",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
