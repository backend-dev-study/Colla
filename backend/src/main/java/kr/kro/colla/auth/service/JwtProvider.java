package kr.kro.colla.auth.service;

import io.jsonwebtoken.*;
import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import kr.kro.colla.exception.exception.auth.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtProvider(
            @Value("${jwt.secret_key}") String secretKey,
            @Value("${jwt.access_token_expiration_time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh_token_expiration_time}") long refreshTokenExpirationTime
    ) {
        this.secretKey = secretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public CreateTokenResponse createTokens(Long id) {
        String accessToken = createAccessToken(id);
        String refreshToken = createRefreshToken(id);

        return new CreateTokenResponse(accessToken, refreshToken);
    }

    public String createAccessToken(Long id) {
        return createToken(id, accessTokenExpirationTime);
    }

    public String createRefreshToken(Long id) {
        return createToken(id, refreshTokenExpirationTime);
    }

    public String createToken(Long value, long expirationTime) {
        Date now = new Date();

        return Jwts.builder()
                .claim("id", value)
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

            return !claimsJws.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch(Exception e) {
            return false;
        }
    }

    public Long findIdFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return Long.parseLong(
                    claimsJws.getBody()
                            .get("id")
                            .toString()
            );
        } catch (ExpiredJwtException e) {
            return Long.parseLong(
                    e.getClaims()
                            .get("id")
                            .toString()
            );
        } catch(Exception e) {
            throw new InvalidTokenException();
        }
    }

}
