package org.coderecon.wanderlkspringbackend.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.coderecon.wanderlkspringbackend.models.User;
import org.coderecon.wanderlkspringbackend.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service

public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean validToken = tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }

    public boolean isValidRefreshToken(String token, User user) {
        System.out.println("Inside isValidRefreshToken");
        String username = extractUsername(token);

        boolean validRefreshToken = tokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        System.out.println("validRefreshToken: " + validRefreshToken);

        return (username.equals(user.getEmail())) && !isTokenExpired(token) && validRefreshToken;
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parser()
//                .verifyWith(getSigninKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(User user, long expiration) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigninKey())
                .compact();

        return token;
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpire);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpire);
    }

}
