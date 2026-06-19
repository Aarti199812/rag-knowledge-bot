package com.ragbot.rag_knowledge_bot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String email ,String role){
         SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
         return Jwts.builder()
             .subject(email)
             .claim("role",role)
             .issuedAt(new Date(System.currentTimeMillis()+expiration))
             .signWith(key)
             .compact();
    }

    public  String getEmailFromToken(String token){
        return getClaims(token).getSubject();
    }

    public String getRoleFromToken(String token){
        return getClaims(token).get("role",String.class);
    }

    public boolean validateToken(String token){
        try {
            getClaims(token);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
    }

