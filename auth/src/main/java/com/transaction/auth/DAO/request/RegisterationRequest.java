package com.transaction.auth.DAO.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record RegisterationRequest(
    @NotBlank (message = "Please the phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    String phone,

    @NotNull 
    @Past
    LocalDate dob,

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72,
          message = "Password must be between 8 and 72 characters")
    String password,


    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    String name
) {
    
}
