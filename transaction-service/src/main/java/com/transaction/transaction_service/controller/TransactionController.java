package com.transaction.transaction_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.transaction_service.dao.ApiResponse;
import com.transaction.transaction_service.dao.request.TopUpRequest;
import com.transaction.transaction_service.dao.request.TransferRequest;
import com.transaction.transaction_service.dao.response.GetTransactionResponse;
import com.transaction.transaction_service.service.TransactionService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/transaction")
@AllArgsConstructor 
public class TransactionController {
    
    private  final TransactionService transactionService;

    @PostMapping("/topup")
    public ResponseEntity<ApiResponse<String>> topUp(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody TopUpRequest request
    ){
        UUID userId = UUID.fromString(jwt.getSubject());
        String message= transactionService.topUp(userId, request);

        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<String>> transfer(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody TransferRequest request
    ){
        UUID senderId = UUID.fromString(jwt.getSubject());
        String message = transactionService.transfer(senderId,request);

        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @GetMapping("/allTransactions")
    public ResponseEntity<ApiResponse<List<GetTransactionResponse>>> getAllTransaction(
        @AuthenticationPrincipal Jwt jwt
    ){
        UUID userID = UUID.fromString(jwt.getSubject());
        List<GetTransactionResponse> allTransaction = transactionService.getAllTransaction(userID);
        return ResponseEntity.ok(ApiResponse.success("All Transaction",allTransaction));
        
    }
}
