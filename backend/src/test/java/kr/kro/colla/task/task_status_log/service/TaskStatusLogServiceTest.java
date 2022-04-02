package kr.kro.colla.task.task_status_log.service;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.TaskStatusProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import kr.kro.colla.task.task_status_log.domain.repository.TaskStatusLogRepository;
import kr.kro.colla.task.task_status_log.presentation.dto.TaskStatusLogResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static kr.kro.colla.common.fixture.TaskStatusLogProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskStatusLogServiceTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskStatusLogRepository taskStatusLogRepository;

    @InjectMocks
    private TaskStatusLogService taskStatusLogService;

    @Test
    void 태스크_상태_로그를_새로_생성한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        TaskStatus taskStatus = TaskStatusProvider.createTaskStatus("To Do");

        given(taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now())))
                .willReturn(Optional.empty());

        // when
        taskStatusLogService.writeTaskStatusLog(project, taskStatus);

        // then
        verify(taskStatusLogRepository, times(1)).findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now()));
        verify(taskStatusLogRepository, times(1)).save(any(TaskStatusLog.class));
    }

    @Test
    void 기존에_해당_날짜의_상태_로그가_존재한다면_새로_생성하지_않고_개수를_증가시킨다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        TaskStatus taskStatus = TaskStatusProvider.createTaskStatus("Done");
        TaskStatusLog taskStatusLog = mock(TaskStatusLog.class);

        given(taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now())))
                .willReturn(Optional.of(taskStatusLog));

        // when
        taskStatusLogService.writeTaskStatusLog(project, taskStatus);

        // then
        verify(taskStatusLogRepository, times(1)).findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now()));
        verify(taskStatusLog, times(1)).increaseCount();
        verify(taskStatusLogRepository, never()).save(any(TaskStatusLog.class));
    }

    @Test
    void 태스크의_상태값이_같은_날짜에_두_번_이상_변경되면_마지막_상태만_로그로_기록한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        TaskStatus prevStatus = TaskStatusProvider.createTaskStatus("To Do");
        TaskStatus newStatus = TaskStatusProvider.createTaskStatus("Done");
        Task task = TaskProvider.createTaskForRepository(null, project, null, newStatus);
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());
        TaskStatusLog prevTaskStatusLog = mock(TaskStatusLog.class);
        TaskStatusLog newTaskStatusLog = mock(TaskStatusLog.class);

        given(taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now())))
                .willReturn(Optional.of(prevTaskStatusLog), Optional.of(newTaskStatusLog));

        // when
        taskStatusLogService.updateTaskStatusLog(project, task, prevStatus, newStatus);

        // then
        verify(taskStatusLogRepository, times(2)).findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now()));
        verify(prevTaskStatusLog, times(1)).decreaseCount();
        verify(newTaskStatusLog, times(1)).increaseCount();
    }

    @Test
    void 태스크_상태값을_삭제할_경우_변경된_상태값의_로그_카운트가_올라간다() {
        // given
        int count = 8;
        Project project = ProjectProvider.createProject(5L);
        TaskStatus taskStatus = TaskStatusProvider.createTaskStatus("In Progress");
        TaskStatusLog taskStatusLog = mock(TaskStatusLog.class);

        given(taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now())))
                .willReturn(Optional.of(taskStatusLog));

        // when
        taskStatusLogService.updateTaskStatusLogForTaskStatusDeletion(project, taskStatus, count);

        // then
        verify(taskStatusLogRepository, times(1)).findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now()));
        verify(taskStatusLog, times(count)).increaseCount();
    }

    @Test
    void 태스크_상태값을_삭제했을_때_변경된_상태값의_로그가_없다면_새로_기록한다() {
        // given
        int count = 10;
        Project project = ProjectProvider.createProject(5L);
        TaskStatus taskStatus = TaskStatusProvider.createTaskStatus("To Do");

        given(taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now())))
                .willReturn(Optional.empty());

        // when
        taskStatusLogService.updateTaskStatusLogForTaskStatusDeletion(project, taskStatus, count);

        // then
        verify(taskStatusLogRepository, times(1)).findTaskStatusLogByProjectAndStatusAndCreatedAt(any(Project.class), anyString(), eq(LocalDate.now()));
        verify(taskStatusLogRepository, times(1)).save(any(TaskStatusLog.class));
    }

    @Test
    void 일주일_단위의_상태_로그를_조회한다() {
        // given
        Long projectId = 1L;
        Project project = ProjectProvider.createProject(5L);
        List<TaskStatus> taskStatusList = List.of(
                TaskStatusProvider.createTaskStatus("To Do"),
                TaskStatusProvider.createTaskStatus("In Progress"),
                TaskStatusProvider.createTaskStatus("Done")
        );
        ReflectionTestUtils.setField(project, "taskStatuses", taskStatusList);

        List<TaskStatusLog> taskStatusLogs = List.of(
                태스크_상태_로그_생성(project, taskStatusList.get(0)),
                태스크_상태_로그_생성(project, taskStatusList.get(2)),
                태스크_상태_로그_생성(project, taskStatusList.get(2))
        );

        given(projectService.findProjectById(eq(projectId)))
                .willReturn(project);
        given(taskStatusLogRepository.findTaskStatusLogsByProjectAndCreatedAtBetween(any(Project.class), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(taskStatusLogs);

        // when
        Map<String, List<TaskStatusLogResponse>> weeklyTaskStatusLog = taskStatusLogService.getWeeklyTaskStatusLog(projectId);

        // then
        assertThat(weeklyTaskStatusLog).hasSize(3);
        assertThat(weeklyTaskStatusLog.get(taskStatusList.get(0).getName())).hasSize(1);
        assertThat(weeklyTaskStatusLog.get(taskStatusList.get(1).getName())).hasSize(0);
        assertThat(weeklyTaskStatusLog.get(taskStatusList.get(2).getName())).hasSize(2);
        verify(taskStatusLogRepository, times(1)).findTaskStatusLogsByProjectAndCreatedAtBetween(any(Project.class), any(LocalDate.class), any(LocalDate.class));
    }

}
