package com.meetingApi.meetingRestApiDemo.businiess.abstracts;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
@Service
public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims,T> claimsResolver);
    Claims extractAllClaims(String token);
    Key getSıgnInKey();
    String genereteToken(UserDetails userDetails);
    String genereteToken(Map<String, Object> extraClaims, UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    Date getExpirationDate(String token);
}
