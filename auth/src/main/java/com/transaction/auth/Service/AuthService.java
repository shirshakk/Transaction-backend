package com.transaction.auth.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.transaction.auth.DAO.request.LoginRequest;
import com.transaction.auth.DAO.request.RegisterationRequest;
import com.transaction.auth.DAO.request.UserProfile;
import com.transaction.auth.DAO.response.TokenResponse;
import com.transaction.auth.Repository.UserRepository;
import com.transaction.auth.event.UserEventProducer;
import com.transaction.auth.model.User;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;
    private final JwtEncoder jwtEncoder;

    private final RefreshTokenService refreshTokenService;
    

    public void register(RegisterationRequest request) {

        User user = new User();

        

        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());

        userRepository.save(user);

        UserProfile userProfile = new UserProfile(
            user.getUser_id(),
            request.phone(),
            request.dob(),
            request.email(),
            request.name()
        );

        userEventProducer.publishUserRegistered(userProfile);
        userEventProducer.userRegisterWallet(user.getUser_id());
    }

    public TokenResponse login(LoginRequest request) {

        User optionalUser = userRepository.findByEmail(request.email());
        if (optionalUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(request.password(), optionalUser.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
         Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("transaction-auth")
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .subject(optionalUser.getUser_id().toString())
                .claim("user-id", optionalUser.getUser_id())
                .claim("email", optionalUser.getEmail())
                .build();

        String accessToken = jwtEncoder.encode(
        JwtEncoderParameters.from(claims)
        ).getTokenValue();

        String refreshToken =
        refreshTokenService.createRefreshToken(
                optionalUser.getUser_id()
        );
       
       return new TokenResponse(
        accessToken,
        refreshToken,
        "Bearer",
        900
        );
    }

    public void logout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
    }
}
