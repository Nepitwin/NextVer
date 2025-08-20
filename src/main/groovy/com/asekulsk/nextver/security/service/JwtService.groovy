package com.asekulsk.nextver.security.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.Key

@Service
class JwtService {
    private final String secretKey = "mySecretKey"
    private final long expirationMs = 86400000

    String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(secretKey.bytes)

        Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes())

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)

            true
        } catch (Exception ignored) {
            false
        }
    }

    String extractUsername(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.bytes)

        def claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()

        claims.subject
    }
}