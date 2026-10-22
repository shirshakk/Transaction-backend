package com.user.user_service.dao.request;

import jakarta.validation.constraints.NotNull;

public record UserRecommendationRequest(
    @NotNull (message = "Input can not be null")
    String inputData
) {

}
