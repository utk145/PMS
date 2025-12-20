package com.pms.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Value("${jwt.issuer}")
    private String issuer;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Missing required property: jwt.secret");
        }

        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("jwt.secret must be Base64 encoded", e);
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 256 bits");
        }

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .issuer(issuer)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(secretKey)   // algorithm inferred (HS256)
                .compact();
    }
}

/**
 * We used @PostConstruct because Spring injects @Value fields after the constructor runs.

     @Value("${jwt.secret}")
     private String secret;
     public JwtUtil() {
         byte[] keyBytes = Base64.getDecoder().decode(secret); // ❌ secret is null
         this.secretKey = Keys.hmacShaKeyFor(keyBytes);
     }

     At constructor time:
        - secret is still null
        - Spring hasn’t injected @Value yet
        → NullPointerException
        Do not mix constructor injection + dependency injection.

        Method 2-
         @Component
         public class JwtUtil {

                 private final SecretKey secretKey;
                 private final long expirationMs;
                 private final String issuer;

                 public JwtUtil(
                     @Value("${jwt.secret}") String secret,
                     @Value("${jwt.expiration-ms}") long expirationMs,
                     @Value("${jwt.issuer}") String issuer
                 ) {
                     byte[] keyBytes = Base64.getDecoder().decode(secret);
                     this.secretKey = Keys.hmacShaKeyFor(keyBytes);
                     this.expirationMs = expirationMs;
                     this.issuer = issuer;
                     }
         }


 */