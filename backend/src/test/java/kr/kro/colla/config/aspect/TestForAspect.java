package kr.kro.colla.config.aspect;

import kr.kro.colla.utils.ProxyInitialized;
import org.springframework.stereotype.Component;

@Component
public class TestForAspect {

    @ProxyInitialized(target = "Test")
    public void correctMethod(Long id, String other) {

    }

    @ProxyInitialized(target = "Test")
    public void parameterWrongMethod(String other) {

    }

    public void annotationWrongMethod(Long id) {

    }
}
