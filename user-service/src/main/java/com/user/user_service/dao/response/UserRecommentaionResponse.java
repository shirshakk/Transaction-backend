package com.user.user_service.dao.response;

import java.util.UUID;

public record UserRecommentaionResponse(
    String fullName,
    UUID userId,
    String phone
) {
    
}
