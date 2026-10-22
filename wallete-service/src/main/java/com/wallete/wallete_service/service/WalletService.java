package com.wallete.wallete_service.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.wallete.wallete_service.dao.request.WalletStatusRequest;
import com.wallete.wallete_service.dao.response.WalletStatusResponse;
import com.wallete.wallete_service.enums.ErrorCode;
import com.wallete.wallete_service.enums.WalletStatus;
import com.wallete.wallete_service.exception.CustomException;
import com.wallete.wallete_service.model.Wallet;
import com.wallete.wallete_service.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class WalletService {
    
    private final WalletRepository walletRepository;

    public void updateWalletStatus(UUID userId,WalletStatusRequest request){
        Optional<Wallet> optionalWallet = walletRepository.findById(userId);

        Wallet wallet = optionalWallet.get();

        wallet.setStatus(request.walletStatus());

        walletRepository.save(wallet);
    }

    public WalletStatusResponse getWalletData(UUID userId){
        Optional<Wallet> optionalWallet = walletRepository.findById(userId);

        Wallet wallet = optionalWallet.get();

        WalletStatusResponse setWalletData= new WalletStatusResponse();
        setWalletData.setBalance(wallet.getBalance());
        setWalletData.setWalletStatus(wallet.getStatus());
        setWalletData.setCurrency(wallet.getCurrency());
        return setWalletData;

    }

    @Transactional 
    public void topUp(UUID userId,BigDecimal amount){
        Optional<Wallet> getUserWallet = walletRepository.findById(userId);
        Wallet userWallet=getUserWallet.get();

        if(userWallet.getStatus().equals(WalletStatus.INACTIVE)){
            throw new CustomException(ErrorCode.WALLET_UNAVAILABLE);
        }

        userWallet.setBalance(userWallet.getBalance().add(amount));
        walletRepository.save(userWallet);
    }

    @Transactional 
    public void moneyTransfer(UUID senderId,UUID receverId,BigDecimal amount){
        Optional<Wallet> getSenderWallet = walletRepository.findById(senderId);
        Wallet senderWallet=getSenderWallet.get();

        Optional<Wallet> getReceverWallet = walletRepository.findById(receverId);
        Wallet receverWallet=getReceverWallet.get();

        if(senderWallet.getStatus().equals(WalletStatus.INACTIVE) || receverWallet.getStatus().equals(WalletStatus.INACTIVE)){
            throw new CustomException(ErrorCode.WALLET_UNAVAILABLE);
        }

        if(senderWallet.getBalance().compareTo(amount)<0){
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receverWallet.setBalance(receverWallet.getBalance().add(amount));
        walletRepository.save(senderWallet);
        walletRepository.save(receverWallet);

    }
}
