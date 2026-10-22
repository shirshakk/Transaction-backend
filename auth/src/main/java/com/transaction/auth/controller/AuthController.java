package com.transaction.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.auth.DAO.ApiResponse;
import com.transaction.auth.DAO.request.LoginRequest;
import com.transaction.auth.DAO.request.RefreshRequest;
import com.transaction.auth.DAO.request.RegisterationRequest;
import com.transaction.auth.DAO.response.TokenResponse;
import com.transaction.auth.Service.AuthService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
        
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(
        @Valid @RequestBody  RegisterationRequest request
    ) {

        System.out.println("Registration controller reached");
        authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Registration successful",
                        null
                ));
    }

    @PostMapping ("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> loginUser(
        @Valid @RequestBody  LoginRequest request
    ) {
        TokenResponse tokenResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        tokenResponse
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutUser(
        @Valid @RequestBody RefreshRequest request
    ) {
        authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }
}
