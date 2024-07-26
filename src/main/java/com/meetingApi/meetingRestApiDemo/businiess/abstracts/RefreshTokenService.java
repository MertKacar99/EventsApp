package com.meetingApi.meetingRestApiDemo.businiess.abstracts;

import com.meetingApi.meetingRestApiDemo.entities.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    RefreshToken verifyRefreshToken(String refreshToken);

}
