package com.meetingApi.meetingRestApiDemo.controller;

import com.meetingApi.meetingRestApiDemo.businiess.abstracts.AuthService;
import com.meetingApi.meetingRestApiDemo.businiess.abstracts.JwtService;
import com.meetingApi.meetingRestApiDemo.businiess.abstracts.RefreshTokenService;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.LoginRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.RefreshTokenRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.RegisterRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.response.AuthResponse;
import com.meetingApi.meetingRestApiDemo.entities.RefreshToken;
import com.meetingApi.meetingRestApiDemo.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public  AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        String acessToken = jwtService.genereteToken(user);
        return  ResponseEntity.ok(AuthResponse.builder()
                        .accessToken(acessToken)
                        .refreshToken(refreshToken.getRefreshToken())
                .build());
      }
}
