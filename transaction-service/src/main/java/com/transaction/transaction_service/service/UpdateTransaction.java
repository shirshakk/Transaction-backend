package com.transaction.transaction_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.transaction.transaction_service.enums.TransactionStatus;
import com.transaction.transaction_service.model.Transaction;
import com.transaction.transaction_service.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class UpdateTransaction {

    private  final TransactionRepository transactionRepository;


    public void transactionUpdate(UUID transactionId,TransactionStatus tStatus){

        Transaction updateTransaction = transactionRepository
            .findById(transactionId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Transaction not found: " + transactionId
                    )
            );

        updateTransaction.setTransactionStatus(tStatus);

        transactionRepository.save(updateTransaction);


    }
}
