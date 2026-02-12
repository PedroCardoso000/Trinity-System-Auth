package com.trinity.manneger.jwt;

import java.util.Date;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private Key signingKey;

    @PostConstruct
    private void initKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            signingKey = Keys.hmacShaKeyFor(keyBytes); // may throw WeakKeyException
        } catch (IllegalArgumentException | WeakKeyException e) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] derived = digest.digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
                signingKey = Keys.hmacShaKeyFor(derived);
            } catch (NoSuchAlgorithmException ex) {
                signingKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
            }
        }
    }

    /**
     * Gera um token JWT para o usuário fornecido.
     * 
     * @param userDetails Detalhes do usuário para o qual o token será gerado.
     * @return O token JWT gerado.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extrai o nome de usuário do token JWT.
     * 
     * @param token O token JWT do qual o nome de usuário será extraído.
     * @return O nome de usuário extraído do token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida o token JWT.
     * 
     * @param token O token JWT a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
