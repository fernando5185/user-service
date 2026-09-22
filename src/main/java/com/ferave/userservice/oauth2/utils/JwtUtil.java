package com.ferave.userservice.oauth2.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component 
public class JwtUtil {
    
    @Value ("${jwt.secret}")
    private String secret;

    @Value ("${jwt.expiration}")
    private long expiration;

    public String generateToken(String userName) {
        return null;
    }

    public String extractUsername(String token) {
        return null;
    }

    public boolean validateToken(String token, String userName) {
        return false;
    }

    public boolean isTokenExpired(String token) {
        return false;
    }
}
