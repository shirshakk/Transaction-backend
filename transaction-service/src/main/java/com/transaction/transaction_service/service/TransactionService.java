package com.transaction.transaction_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.transaction.transaction_service.Exception.CustomException;
import com.transaction.transaction_service.dao.request.TopUpRequest;
import com.transaction.transaction_service.dao.request.TransferEventRequest;
import com.transaction.transaction_service.dao.request.TransferRequest;
import com.transaction.transaction_service.dao.response.GetTransactionResponse;
import com.transaction.transaction_service.enums.ErrorCode;
import com.transaction.transaction_service.enums.TransactionStatus;
import com.transaction.transaction_service.enums.TransactionType;
import com.transaction.transaction_service.event.EventService;
import com.transaction.transaction_service.model.Transaction;
import com.transaction.transaction_service.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final EventService eventService;
    
    public String topUp(UUID userId,TopUpRequest request){
        if(request.amount().signum() <0){
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }

        Transaction newTransaction= new Transaction();

        newTransaction.setAmount(request.amount());
        newTransaction.setToWalletId(userId);
        newTransaction.setTransactionType(TransactionType.TOPUP);
        newTransaction.setTransactionStatus(TransactionStatus.PENDING);
        newTransaction.setUserId(userId);

        transactionRepository.save(newTransaction);
        
        eventService.userTopUp(newTransaction.getTransactionId(),userId, request.amount());
        return "Transaction recorded";
    }


    public List<GetTransactionResponse> getAllTransaction(UUID userId){
        List<Transaction> allTransactions= transactionRepository.findByUserId(userId);
        
         return allTransactions.stream()
            .map(transaction -> new GetTransactionResponse(
                   transaction.getAmount(),
                   transaction.getFromWalletId(),
                   transaction.getToWalletId(),
                   transaction.getTransactionStatus(),
                   transaction.getTransactionType(),
                   transaction.getTransactionId() 
                   
            ))
            .toList();

    }

    
    public String transfer(UUID senderId,TransferRequest request){
        System.out.println("SenderId="+senderId);
        System.out.println("Request="+request);
        if(request.receverId() == null){
            throw new CustomException(ErrorCode.INVALID_SENDER_ID);
        }
        if(request.amount() == null){
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }
        Transaction newTransaction= new Transaction();
        newTransaction.setAmount(request.amount());
        newTransaction.setFromWalletId(senderId);
        newTransaction.setToWalletId(request.receverId());
        newTransaction.setTransactionType(TransactionType.TRANSFER);
        newTransaction.setTransactionStatus(TransactionStatus.PENDING);
        newTransaction.setUserId(senderId);
        transactionRepository.save(newTransaction);
        System.out.print("TransactionID="+newTransaction.getTransactionId());
        TransferEventRequest eventTransferRequest= new TransferEventRequest(newTransaction.getTransactionId(),senderId,request.receverId(),request.amount());
        eventService.transfer(eventTransferRequest);
        
        return "Transaction recorded";
    }

}
