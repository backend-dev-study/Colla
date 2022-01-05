package kr.kro.colla.auth.service;

import io.jsonwebtoken.*;
import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.access_token_expiration_time}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh_token_expiration_time}")
    private long refreshTokenExpirationTime;

    public CreateTokenResponse createTokens(String userId) {
        Date now = new Date();
        String accessToken = createToken(userId, now, accessTokenExpirationTime);
        String refreshToken = createToken(userId, now, refreshTokenExpirationTime);

        return new CreateTokenResponse(accessToken, refreshToken);
    }

    public String createToken(String value, Date now, long expirationTime) {
        return Jwts.builder()
                .claim("userId", value)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return claimsJws.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch(Exception e) {
            return false;
        }
    }

    public String parseToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return (String) claimsJws.getBody()
                    .get("userId");
        } catch (Exception e) {
            return null;
        }
    }

}
