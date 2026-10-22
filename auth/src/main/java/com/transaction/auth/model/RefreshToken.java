package com.transaction.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
@Entity 
@Data 
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token_id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column (nullable = false)
    private String token_hash;

    @Column (nullable = false)
    private String token_revoked;

    @Column (nullable = false)
    private String token_expired;

    @CreationTimestamp 
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

}
