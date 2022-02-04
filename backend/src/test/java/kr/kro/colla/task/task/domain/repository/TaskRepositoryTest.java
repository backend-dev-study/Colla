package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import kr.kro.colla.task.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    void 프로젝트에_새로운_태스크를_등록한다() {
        // given
        Project project = Project.builder()
                .name("collaboration")
                .description("collaboration tool")
                .managerId(1L)
                .build();
        projectRepository.save(project);

        TaskStatus taskStatus = taskStatusRepository.findByName("To Do")
                .orElseThrow(TaskStatusNotFoundException::new);
        Task task = Task.builder()
                .title("task title")
                .description("task description")
                .project(project)
                .taskStatus(taskStatus)
                .build();

        // when
        Task result = taskRepository.save(task);

        // then
        assertThat(result.getTitle()).isEqualTo(task.getTitle());
        assertThat(result.getTaskStatus().getName()).isEqualTo("To Do");
        assertThat(result.getProject().getName()).isEqualTo(project.getName());
    }

}
