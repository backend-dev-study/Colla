package kr.kro.colla.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;
    private final long accessTokenExpirationTime;

    public JwtProvider(
            @Value("${jwt.secret_key}") String secretKey,
            @Value("${jwt.access_token_expiration_time}") long accessTokenExpirationTime
    ) {
        this.secretKey = secretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    public String createToken(String userId) {
        Date now = new Date();

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
