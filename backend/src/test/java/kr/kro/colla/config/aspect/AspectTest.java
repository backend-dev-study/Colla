package kr.kro.colla.config.aspect;

import kr.kro.colla.task.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AspectTest {
    @Autowired
    private TaskService taskService;

    @Test
    void checkJoinPoint() {
        taskService.testProxy();
    }
}
