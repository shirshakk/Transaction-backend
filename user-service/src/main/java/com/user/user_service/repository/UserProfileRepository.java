package com.user.user_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user_service.model.User;

public interface UserProfileRepository extends JpaRepository<User, UUID> {
    List<User> findByFullNameStartingWithIgnoreCaseOrPhoneNumberStartingWith(
    String fullName, String phoneNumber, Pageable pageable);
}
