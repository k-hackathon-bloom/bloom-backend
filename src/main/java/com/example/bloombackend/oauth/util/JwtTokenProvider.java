package com.example.bloombackend.oauth.util;

import com.example.bloombackend.global.exception.RestApiException;
import com.example.bloombackend.global.exception.TokenError;
import com.example.bloombackend.oauth.token.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret_key}")
    private String encodedSecretKey;

    @Value("${jwt.access_token.valid_time}")
    private Long accessTokenValidSeconds;

    @Value("${jwt.refresh_token.valid_time}")
    private Long refreshTokenValidSeconds;

    private Key secretKey;

    @PostConstruct
    private void decodeKey() {
        byte[] keyBytes = Base64.getDecoder().decode(encodedSecretKey);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String userId) {
        return createToken(userId, accessTokenValidSeconds, TokenType.ACCESS_TOKEN);
    }

    public String createRefreshToken(String userId) {
        return createToken(userId, refreshTokenValidSeconds, TokenType.REFRESH_TOKEN);
    }

    public String refreshAccessToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        String userId = String.valueOf(getUserIdFromToken(refreshToken));
        return createAccessToken(userId);
    }

    private String createToken(String userId, long validSeconds, TokenType tokenType) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validSeconds * 1000);

        return Jwts.builder()
                .setSubject(userId)
                .claim("token_type", tokenType.getValue())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateAccessToken(String accessToken) {
        try {
            Claims claims = parseToken(accessToken);
            String tokenType = (String) claims.get("token_type");

            if (!tokenType.equals(TokenType.ACCESS_TOKEN.getValue())) {
                throw new RestApiException(TokenError.NOT_ACCESS_TOKEN);
            }
        } catch (ExpiredJwtException e) {
            throw new RestApiException(TokenError.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RestApiException(TokenError.INVALID_ACCESS_TOKEN);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            Claims claims = parseToken(refreshToken);
            String tokenType = (String) claims.get("token_type");

            if (!tokenType.equals(TokenType.REFRESH_TOKEN.getValue())) {
                throw new RestApiException(TokenError.NOT_REFRESH_TOKEN);
            }
        } catch (ExpiredJwtException e) {
            throw new RestApiException(TokenError.EXPIRED_REFRESH_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RestApiException(TokenError.INVALID_REFRESH_TOKEN);
        }
    }

    private Claims parseToken(final String refreshToken) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(refreshToken).getBody();
    }

    public LocalDateTime getExpirationTime(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
    }

    public String hashToken(String token) {
        return sha256Hex(token);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }
}