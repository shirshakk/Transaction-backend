package com.user.user_service.controller;

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

import com.user.user_service.dao.ApiResponse;
import com.user.user_service.dao.request.UserProfileRequest;
import com.user.user_service.dao.request.UserRecommendationRequest;
import com.user.user_service.dao.response.UserProfileResponse;
import com.user.user_service.dao.response.UserRecommentaionResponse;
import com.user.user_service.service.UserService;

import jakarta.validation.Valid;


@RestController 
@RequestMapping ("/api/users")
 
public class UserController {

    
    private final UserService userService;
     public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUser(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success("User Details", userService.getUserProfile(userId)   ));

    }

    @GetMapping ("/check")
    public ResponseEntity<ApiResponse<String>> check() {
        return ResponseEntity.ok(ApiResponse.success("User Service is up and running", "User Service is up and running"));
    }

    @PostMapping ("/user")
    public ResponseEntity<ApiResponse<String>> updateUser(
        @AuthenticationPrincipal Jwt jwt, 
        @RequestBody UserProfileRequest request) {

        UUID userId = UUID.fromString(jwt.getSubject());
        userService.updateUserProfile(userId, request);
        
        return ResponseEntity.ok(ApiResponse.success("User updated  successfully",null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserRecommentaionResponse>>> searchUser(
        @Valid @RequestBody UserRecommendationRequest request
    ){
        List<UserRecommentaionResponse> getSearchUser= userService.searchUser(request.inputData());

        return ResponseEntity.ok(ApiResponse.success("User Recommendation",getSearchUser));
    }

}
