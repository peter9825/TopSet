package com.peter.topset.Users.Security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class JwtService {

@Value("${jwt.secret}")
private String secretKey;

@Value("${jwt.expiration}")
private long jwtExpiration;


public String generateToken(Authentication authentication){
    String email = authentication.getName();
    Date currentDate = new Date();
    Date expirationDate = new Date(currentDate.getTime() + jwtExpiration);

    return Jwts.builder()
            .subject(email)
            .issuedAt(currentDate)
            .expiration(expirationDate)
            .signWith(getSigningKey())
            .compact();
}

public String getUsername(String token){
 return Jwts.parser().
                 verifyWith(getSigningKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload()
                 .getSubject();
}


public boolean validateToken(String token){
    try{
        SecretKey key = getSigningKey();
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        return true;
    } catch (SecurityException | MalformedJwtException e) {
        throw new AuthenticationCredentialsNotFoundException("Jwt expired or incorrect. ");
    }
}

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
