package com.toki.common.utils;

import com.toki.common.exception.LeaseException;
import com.toki.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
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

    public static Claims parseToken(String token) {

        if (token == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }

        try {
            final JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SIGN_KEY)
                    .build();
            final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}
