package com.meetingApi.meetingRestApiDemo.businiess.concretes;

import com.meetingApi.meetingRestApiDemo.businiess.abstracts.AuthService;
import com.meetingApi.meetingRestApiDemo.businiess.abstracts.JwtService;
import com.meetingApi.meetingRestApiDemo.businiess.abstracts.RefreshTokenService;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.LoginRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.RegisterRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.response.AuthResponse;
import com.meetingApi.meetingRestApiDemo.dataacess.UserRepository;
import com.meetingApi.meetingRestApiDemo.entities.User;
import com.meetingApi.meetingRestApiDemo.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthManager implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;



    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
       var user = User.builder()
               .name(registerRequest.getName())
               .email(registerRequest.getEmail())
               .username(registerRequest.getUsername())
               .password(passwordEncoder.encode(registerRequest.getPassword()))
               .authorities(Set.of(Role.ROLE_USER))
               .build();
       User savedUser = userRepository.save(user);
       var accessToken  = jwtService.genereteToken(savedUser);  //TODO JWTSERVİCE KODLANDIKTAN SONRA GEL.
       var refreshToken =   refreshTokenService.createRefreshToken(savedUser.getEmail());

       return AuthResponse.builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken.getRefreshToken())
               .build();

    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       loginRequest.getEmail(),
                       loginRequest.getPassword()
               )
       );
       var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
       -> new UsernameNotFoundException("User not found"));
       var acessToken = jwtService.genereteToken(user);

       var refreshToken =   refreshTokenService.createRefreshToken(loginRequest.getEmail());


       return AuthResponse.builder()
               .accessToken(acessToken)
               .refreshToken(refreshToken.getRefreshToken())
               .build();
    }
}
