package com.wallete.wallete_service.controller;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallete.wallete_service.dao.ApiResponse;
import com.wallete.wallete_service.dao.request.WalletStatusRequest;
import com.wallete.wallete_service.dao.response.WalletStatusResponse;
import com.wallete.wallete_service.service.WalletService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/digital")
@AllArgsConstructor 
public class WalletController {
    
    private  final WalletService walletService;

    @PatchMapping("/wallet/status")
    public ResponseEntity<ApiResponse<String>> updateWalletStatus(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody WalletStatusRequest request
    ){
        UUID userId=  UUID.fromString(jwt.getSubject());

        walletService.updateWalletStatus(userId, request);

        return ResponseEntity.ok(ApiResponse.success("Updated Wallet Status", null));
    }

    @GetMapping("/wallet")
    public ResponseEntity<ApiResponse<WalletStatusResponse>> getWalletData(
        @AuthenticationPrincipal  Jwt jwt
    ){
        UUID userId=  UUID.fromString(jwt.getSubject());
        WalletStatusResponse getWallet = walletService.getWalletData(userId);

        return  ResponseEntity.ok(ApiResponse.success("Wallet Details", getWallet));
    }
}
