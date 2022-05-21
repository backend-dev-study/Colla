package kr.kro.colla.config.aspect;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AspectTestConfig {

    @Bean
    public TestForAspect TestForAspect() {
        return new TestForAspect();
    }
}
