package com.nimbusair.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Operation(summary = "Generar token desde entidad User", description = "Genera un token JWT a partir de la entidad User completa")
    public String generateToken(
            @Parameter(description = "Entidad User", required = true) com.nimbusair.entity.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("name", user.getName());
        claims.put("role", user.getRole().name());
        return createToken(claims, user.getEmail());
    }

    @Operation(summary = "Generar token con userId", description = "Genera un token JWT que incluye el ID del usuario")
    public String generateToken(
            @Parameter(description = "Detalles del usuario", required = true) UserDetails userDetails,
            @Parameter(description = "ID del usuario", required = true) Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, userDetails.getUsername());
    }

    @Operation(summary = "Generar token básico", description = "Genera un token JWT sin información adicional")
    public String generateToken(
            @Parameter(description = "Detalles del usuario", required = true) UserDetails userDetails) {
        return generateToken(userDetails, 0L);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    @Operation(summary = "Extraer username del token", description = "Extrae el email/username del token JWT")
    public String extractUsername(
            @Parameter(description = "Token JWT", required = true) String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Operation(summary = "Extraer userId del token", description = "Extrae el ID del usuario del token JWT")
    public Long extractUserId(
            @Parameter(description = "Token JWT", required = true) String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            }
            return 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}