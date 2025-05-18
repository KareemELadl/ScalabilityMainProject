package com.example.Transaction.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "my_super_secret_key_12345678901234567890";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    private static JwtService instance;

    private JwtService() {
    }

    public static JwtService getInstance() {
        if (instance == null) {
            synchronized (JwtService.class) {
                if (instance == null) {
                    instance = new JwtService();
                }
            }
        }
        return instance;
    }

    // Generate a simple token with just email as subject
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email) // Still useful as the main identity
                .claim("userId", userId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 mins
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract user ID
    public Long extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    // Extract email (still available as subject or claim)
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    // Check token expiration
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
