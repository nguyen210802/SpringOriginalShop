package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.request.AuthenticationRequest;
import com.example.identityService.dto.request.IntrospectRequest;
import com.example.identityService.dto.response.AuthenticationResponse;
import com.example.identityService.dto.response.IntrospectResponse;
import com.example.identityService.dto.response.RefreshTokenRequest;
import com.example.identityService.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    Map<String, AuthenticationService> map;

    public AuthenticationController(AuthenticationService authenticationServiceImpl) {
        this.map = Map.of("auth", authenticationServiceImpl);
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(map.get("auth").authenticate(request))
                .build();
    }

    @PostMapping("/loginWithGoogle")
    public ApiResponse<AuthenticationResponse> authenticateWithGoogle(@AuthenticationPrincipal OAuth2User principal){
        log.info("authenticateWithGoogle: {}", principal);
        return ApiResponse.<AuthenticationResponse>builder()
               .result(map.get("auth").authenticateWithGoogle(principal))
               .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspectResponse(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        log.info("introspect: {}", request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(map.get("auth").introspect(request))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(map.get("auth").refreshToken(request))
                .build();
    }
}
