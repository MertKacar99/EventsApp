package com.meetingApi.meetingRestApiDemo.businiess.concretes;

import com.meetingApi.meetingRestApiDemo.businiess.abstracts.RefreshTokenService;
import com.meetingApi.meetingRestApiDemo.dataacess.RefreshTokenRepository;
import com.meetingApi.meetingRestApiDemo.dataacess.UserRepository;
import com.meetingApi.meetingRestApiDemo.entities.RefreshToken;
import com.meetingApi.meetingRestApiDemo.entities.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenManager implements RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenManager(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found" + username));
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            long refreshTokenValidty = 30 * 1000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidty))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Refresh token not found!"));
        //compareTo yöntemi, iki nesnenin birbirine göre sıralama düzenini belirler. Instant sınıfında bu yöntem, iki zaman damgasını karşılaştırır ve sonucu bir tamsayı olarak döndürür:
        //
        //Negatif Değer: İlk zaman damgası ikinci zaman damgasından önceyse.
        //Sıfır (0): İki zaman damgası eşitse.
        //Pozitif Değer: İlk zaman damgası ikinci zaman damgasından sonra ise.
        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token expired!");
        }
        return refToken;
    }
}
