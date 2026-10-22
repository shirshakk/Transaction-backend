package com.transaction.auth.DAO.request;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfile(
    UUID user_id,
    String phone,
    LocalDate dob,
    String email,
    String name
) {}