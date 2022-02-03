package kr.kro.colla.project.task_status.domain.repository;

import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class TaskStatusRepositoryTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    void 태스크_상태를_이름으로_조회한다() {
        // given
        String name = "In Progress";
        TaskStatus taskStatus = new TaskStatus(name);
        taskStatusRepository.save(taskStatus);

        // when
        TaskStatus result = taskStatusRepository.findByName(name)
                .orElseThrow(TaskStatusNotFoundException::new);

        // then
        assertThat(result.getName()).isEqualTo(name);
    }

}
