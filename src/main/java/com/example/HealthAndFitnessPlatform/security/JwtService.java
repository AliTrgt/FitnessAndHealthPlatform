package com.example.HealthAndFitnessPlatform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    private int EXPIRATION_DATE = 1000 * 3600 * 24 * 5; // 5 gün

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUser(token);
        Date expirationDate = extractExpirationDate(token);
            return userDetails.getUsername().equals(username) && !expirationDate.before(expirationDate);
    }

    public String extractUser(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Date extractExpirationDate(String token){
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration();
    }

    public String generateToken(String username){
        Map  <String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .signWith(generateKey())
                .compact();
    }

    public Key generateKey(){
       byte[] hashKey = Decoders.BASE64URL.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(hashKey);
    }

}
