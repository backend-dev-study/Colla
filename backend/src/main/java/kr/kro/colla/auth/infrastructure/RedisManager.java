package kr.kro.colla.auth.infrastructure;

import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisManager {

    @Value("${jwt.refresh_token_expiration_time}")
    private long refreshTokenExpirationTime;

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(CreateTokenResponse createTokenResponse) {
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        valueOperations.set(
                createTokenResponse.getAccessToken(),
                createTokenResponse.getRefreshToken(),
                refreshTokenExpirationTime,
                TimeUnit.MILLISECONDS
        );
    }

}
