package kr.kro.colla.task.task_status_log.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static kr.kro.colla.common.fixture.TaskStatusLogProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@EnableJpaAuditing
@DataJpaTest
class TaskStatusLogRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskStatusLogRepository taskStatusLogRepository;

    @Test
    void 태스크_상태_로그를_저장한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(1L));
        TaskStatus taskStatus = project.getTaskStatuses().get(0);
        Task task = taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus));
        TaskStatusLog taskStatusLog = 태스크_상태_로그_생성(project, task, taskStatus);

        // when
        TaskStatusLog result = taskStatusLogRepository.save(taskStatusLog);

        // then
        assertThat(result.getProject().getId()).isEqualTo(project.getId());
        assertThat(result.getTask().getId()).isEqualTo(task.getId());
        assertThat(result.getStatus()).isEqualTo(taskStatus.getName());
        assertThat(result.getCreatedAt()).isEqualTo(LocalDate.now());
    }

    @Test
    void 현재_프로젝트의_특정_태스크_상태_로그를_조회한다() {
        // given
        Project firstProject = projectRepository.save(ProjectProvider.createProject(1L));
        Project secondProject = projectRepository.save(ProjectProvider.createProject(5L));
        TaskStatus taskStatus1ForFirstProject = firstProject.getTaskStatuses().get(0);
        TaskStatus taskStatus2ForFirstProject = firstProject.getTaskStatuses().get(2);
        TaskStatus taskStatusForSecondProject = secondProject.getTaskStatuses().get(0);

        Task task1 = taskRepository.save(TaskProvider.createTaskForRepository(null, firstProject, null, taskStatus1ForFirstProject));
        Task task2 = taskRepository.save(TaskProvider.createTaskForRepository(null, secondProject, null, taskStatusForSecondProject));
        Task task3 = taskRepository.save(TaskProvider.createTaskForRepository(null, firstProject, null, taskStatus2ForFirstProject));

        taskStatusLogRepository.save(태스크_상태_로그_생성(firstProject, task1, task1.getTaskStatus()));
        taskStatusLogRepository.save(태스크_상태_로그_생성(secondProject, task2, task2.getTaskStatus()));
        taskStatusLogRepository.save(태스크_상태_로그_생성(firstProject, task3, task3.getTaskStatus()));

        // when
        TaskStatusLog result = taskStatusLogRepository.findTaskStatusLogByProjectAndTaskAndStatus(firstProject, task3, taskStatus2ForFirstProject.getName())
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(result.getStatus()).isEqualTo(taskStatus2ForFirstProject.getName());
        assertThat(result.getProject().getId()).isEqualTo(firstProject.getId());
        assertThat(result.getTask().getId()).isEqualTo(task3.getId());
    }

}
