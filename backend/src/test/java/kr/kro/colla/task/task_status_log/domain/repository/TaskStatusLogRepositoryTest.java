package kr.kro.colla.task.task_status_log.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static kr.kro.colla.common.fixture.TaskStatusLogProvider.태스크_상태_로그_생성;
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
    void 태스크_상태_로그를_생성한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(1L));
        TaskStatus taskStatus = project.getTaskStatuses().get(0);
        TaskStatusLog taskStatusLog = 태스크_상태_로그_생성(project, taskStatus);

        // when
        TaskStatusLog result = taskStatusLogRepository.save(taskStatusLog);

        // then
        assertThat(result.getProject().getId()).isEqualTo(project.getId());
        assertThat(result.getStatus()).isEqualTo(taskStatus.getName());
        assertThat(result.getCount()).isOne();
        assertThat(result.getCreatedAt()).isEqualTo(LocalDate.now());
    }

    @Test
    void 현재_프로젝트에서_오늘_날짜의_특정_상태_로그를_조회한다() {
        // given
        Project firstProject = projectRepository.save(ProjectProvider.createProject(1L));
        Project secondProject = projectRepository.save(ProjectProvider.createProject(5L));
        TaskStatus taskStatus1 = firstProject.getTaskStatuses().get(0);
        TaskStatus taskStatus2 = firstProject.getTaskStatuses().get(2);
        TaskStatus taskStatus3 = secondProject.getTaskStatuses().get(1);

        taskStatusLogRepository.saveAll(List.of(
                태스크_상태_로그_생성(firstProject, taskStatus1),
                태스크_상태_로그_생성(firstProject, taskStatus2),
                태스크_상태_로그_생성(secondProject, taskStatus3)
        ));

        // when
        TaskStatusLog taskStatusLog = taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(firstProject, taskStatus2.getName(), LocalDate.now())
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(taskStatusLog.getStatus()).isEqualTo(taskStatus2.getName());
        assertThat(taskStatusLog.getProject().getId()).isEqualTo(firstProject.getId());
    }

    @Test
    void 현재_프로젝트에_오늘_날짜의_특정_상태_로그가_없다면_빈_값을_반환한다() {
        Project firstProject = projectRepository.save(ProjectProvider.createProject(1L));
        Project secondProject = projectRepository.save(ProjectProvider.createProject(5L));
        TaskStatus taskStatus1 = firstProject.getTaskStatuses().get(0);
        TaskStatus taskStatus2 = secondProject.getTaskStatuses().get(1);

        taskStatusLogRepository.saveAll(List.of(
                태스크_상태_로그_생성(firstProject, taskStatus1),
                태스크_상태_로그_생성(secondProject, taskStatus2)
        ));

        // when
        Optional<TaskStatusLog> taskStatusLog = taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(secondProject, taskStatus1.getName(), LocalDate.now());

        // then
        assertThat(taskStatusLog).isEmpty();
    }

}

