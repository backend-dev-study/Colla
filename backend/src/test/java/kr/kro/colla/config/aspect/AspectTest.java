package kr.kro.colla.config.aspect;

import kr.kro.colla.utils.ProxyInitialized;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import static org.assertj.core.api.Assertions.assertThat;

public class AspectTest {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    @BeforeEach
    void setUp() {
        pointcut.setExpression("@annotation(kr.kro.colla.utils.ProxyInitialized) && args(Long,..)");
    }

    @Test
    void 조건에_해당되면_포인트컷과_매칭된다() throws NoSuchMethodException {
        boolean isMatch = pointcut.matches(AspectTarget.class.getMethod("correctMethod", Long.class, String.class), AspectTarget.class);
        assertThat(isMatch).isTrue();
    }

    @Test
    void 어노테이션이_명시되지_않으면_매칭되지_않는다() throws NoSuchMethodException {
        boolean isMatch = pointcut.matches(AspectTarget.class.getMethod("annotationWrongMethod", Long.class), AspectTarget.class);
        assertThat(isMatch).isFalse();
    }

    @Test
    void 파라미터_타입이_올바르지_않으면_매칭되지_않는다() throws NoSuchMethodException {
        boolean isMatch = pointcut.matches(AspectTarget.class.getMethod("parameterWrongMethod", String.class), AspectTarget.class);
        assertThat(isMatch).isFalse();
    }
}

class AspectTarget {

    @ProxyInitialized(target = "Test")
    public void correctMethod(Long id, String other) {}

    @ProxyInitialized(target = "Test")
    public void parameterWrongMethod(String other) {}

    public void annotationWrongMethod(Long id) {}

}
