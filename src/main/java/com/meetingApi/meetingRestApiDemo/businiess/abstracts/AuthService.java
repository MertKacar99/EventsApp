package com.meetingApi.meetingRestApiDemo.businiess.abstracts;

import com.meetingApi.meetingRestApiDemo.businiess.dto.request.LoginRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.RegisterRequest;
import com.meetingApi.meetingRestApiDemo.businiess.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
  AuthResponse register(RegisterRequest registerRequest);
  AuthResponse login(LoginRequest loginRequest);
}
