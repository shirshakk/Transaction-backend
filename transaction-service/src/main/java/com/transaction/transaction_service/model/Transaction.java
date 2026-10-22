package com.transaction.transaction_service.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.transaction.transaction_service.enums.TransactionType;

import com.transaction.transaction_service.enums.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity 
@Data 
@Table(name = "transaction")
public class Transaction {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID transactionId;

    @NotNull(message = "User Id can not be null")
    UUID userId;

    @NotNull(message = "Transaction type can not be null")
    @Enumerated(EnumType.STRING)
    TransactionType transactionType;

    UUID fromWalletId;

    UUID toWalletId;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    TransactionStatus transactionStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
}
