package com.example.identityService.service.impl;

import com.example.identityService.dto.request.AuthenticationRequest;
import com.example.identityService.dto.request.IntrospectRequest;
import com.example.identityService.dto.response.AuthenticationResponse;
import com.example.identityService.dto.response.IntrospectResponse;
import com.example.identityService.dto.response.RefreshTokenRequest;
import com.example.identityService.entity.User;
import com.example.identityService.enums.Role;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null)
            return AuthenticationResponse.builder()
                    .authenticated(false)
                    .build();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated)
            return AuthenticationResponse.builder()
                    .authenticated(false)
                    .build();

        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .refreshToken(generateRefreshToken(user))
                .authenticated(true)
                .build();
    }

    @Override
    public AuthenticationResponse authenticateWithGoogle(OAuth2User principal) {
        var token = generateToken(principal);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier jwsVerifier =new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT =SignedJWT.parse(token);

        Date expiry =signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified =signedJWT.verify(jwsVerifier);

        return IntrospectResponse.builder()
                .valid(verified && expiry.after(new Date()))
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.getRefreshToken());
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid refresh token");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date())) {
                throw new RuntimeException("Refresh token expired");
            }

            if (!"refresh".equals(claims.getClaim("type"))) {
                throw new RuntimeException("Invalid token type");
            }

            String userId = claims.getSubject();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            String newToken = generateToken(user);
            String newRefreshToken = generateRefreshToken(user);

            return AuthenticationResponse.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error refreshing token");
        }
    }

    private String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("nguyen.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .claim("scope", user.getRole())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    private String generateRefreshToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("nguyen.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("type", "refresh")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create refresh token");
            throw new RuntimeException(e);
        }
    }

    private String generateToken(OAuth2User principal){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(Objects.requireNonNull(principal.getAttribute("id"))))
                .issuer("nguyen.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("scope", Role.USER)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }
}
