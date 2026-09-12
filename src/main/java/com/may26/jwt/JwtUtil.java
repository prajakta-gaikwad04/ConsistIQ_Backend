package com.may26.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    // Generate JWT Token
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000L * 60 * 60 * 24 * 30
                        )
                )
                .signWith(key())
                .compact();
    }
    public String extractRole(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }
    // Extract Email From Token
    public String extractEmail(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Validate Token
    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // Secret Key Method
    private SecretKey key() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
                
        );
    }
}