package com.user.user_service.dao.response;

import java.time.LocalDate;


public record UserProfileResponse(
    String name,

    String email,

    String phone,

    LocalDate dob,

    String profileImageUrl
) {
} 
