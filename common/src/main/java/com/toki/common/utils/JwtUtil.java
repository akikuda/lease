package com.toki.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author toki
 */
public class JwtUtil {

    private static final long TOKEN_EXPIRATION = 60 * 60 * 1000L;
    private static final SecretKey TOKEN_SIGN_KEY
            = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Long userId, String username) {

        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .signWith(TOKEN_SIGN_KEY, SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .setSubject("LOGIN_USER")
                .compact();

    }
}
