//package com.example.Transaction.Service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//
//@Service
//public class JwtService {
//    private final Key secretKey = Keys.hmacShaKeyFor("my_super_secret_key_12345678901234567890".getBytes());
//
//    /** Extracts claims if token valid; else throws. */
//    public Claims parseToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    /** Validate signature + expiration */
//    public boolean isTokenValid(String token) {
//        try {
//            parseToken(token);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }
//
//    /** Optionally extract username/userId from claims */
//    public String getUsername(String token) {
//        return parseToken(token).getSubject();
//    }
//}
