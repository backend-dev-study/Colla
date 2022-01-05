package kr.kro.colla.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void key_value_쌍으로_데이터를_저장한다() {
        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "kykapple", value = "hello";

        // when
        valueOperations.set(key, value);

        // then
        String result = valueOperations.get(key);
        assertThat(result).isEqualTo(value);
    }

}
