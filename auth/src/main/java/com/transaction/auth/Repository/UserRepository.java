package com.transaction.auth.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transaction.auth.model.User;

@Repository 
public interface UserRepository extends JpaRepository<User, UUID> {
    
    User findByEmail(String email);
}
