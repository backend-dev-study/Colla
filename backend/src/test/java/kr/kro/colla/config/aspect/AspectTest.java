package kr.kro.colla.config.aspect;

import kr.kro.colla.task.task.service.TaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AspectTestConfig.class)
@SpringBootTest
public class AspectTest {
    @Autowired
    private TaskService taskService;

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    @BeforeEach
    void setUp() {
        pointcut.setExpression("@annotation(kr.kro.colla.utils.ProxyInitialized) && args(Long,..)");
    }

    @Test
    void 조건에_해당되면_포인트컷과_매칭된다() throws NoSuchMethodException {
        boolean isMatch = pointcut.matches(TestForAspect.class.getMethod("correctMethod", Long.class, String.class), TestForAspect.class);
        assertThat(isMatch).isTrue();
    }

    @Test
    void 어노테이션이_명시되지_않으면_매칭되지_않는다() throws NoSuchMethodException {
        boolean isMatch = pointcut.matches(TestForAspect.class.getMethod("annotationWrongMethod", Long.class), TestForAspect.class);
        assertThat(isMatch).isFalse();
    }

    @Test
    void 파라미터_타입이_올바르지_않으면_매칭되지_않는다() throws NoSuchMethodException {
        boolean isMatch = pointcut.matches(TestForAspect.class.getMethod("parameterWrongMethod", String.class), TestForAspect.class);
        assertThat(isMatch).isFalse();
    }
}
