package com.user.user_service.dao.request;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfileRequest(
    UUID user_id,

    String name,

    String email,

    String phone,

    LocalDate dob,

    String profileImageUrl
) {
    
}
