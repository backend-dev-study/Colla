package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.exception.exception.task.TaskNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void 프로젝트의_태스크를_조회한다() {
        // given
        Project project = Project.builder()
                .name("collaboration")
                .description("collaboration tool")
                .managerId(1L)
                .build();
        projectRepository.save(project);

        Task task = Task.builder()
                .title("task title")
                .description("task description")
                .project(project)
                .build();
        taskRepository.save(task);

        // when
        Task result = taskRepository.findById(task.getId())
                .orElseThrow(TaskNotFoundException::new);

        // then
        assertThat(result.getTitle()).isEqualTo(task.getTitle());
        assertThat(result.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    void 존재하지_않는_태스크를_조회할_경우_예외가_발생한다() {
        // when, then
        assertThatThrownBy(
                () -> taskRepository.findById(100L)
                        .orElseThrow(TaskNotFoundException::new)
        ).isInstanceOf(TaskNotFoundException.class);
    }

}
