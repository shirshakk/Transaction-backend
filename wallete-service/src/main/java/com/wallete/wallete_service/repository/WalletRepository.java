package com.wallete.wallete_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallete.wallete_service.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    
}
