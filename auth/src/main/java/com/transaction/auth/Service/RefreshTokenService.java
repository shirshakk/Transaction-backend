package com.transaction.auth.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

import com.transaction.auth.Repository.RefreshTokenRepository;
import com.transaction.auth.model.RefreshToken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional 
    public String createRefreshToken(UUID userId){
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken_hash(tokenHash);

         refreshToken.setToken_expired(
                java.time.Instant.now()
                        .plus(7, java.time.temporal.ChronoUnit.DAYS)
                        .toString()
        );

        refreshToken.setToken_revoked("false");

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }


    private String hashToken(String token) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "SHA-256 algorithm unavailable", ex
            );
        }
    }

    @Transactional 
    public void deleteRefreshToken(String refreshToken) {
        String tokenHash = hashToken(refreshToken);
        refreshTokenRepository.deleteByTokenHash(tokenHash);
    }
}
