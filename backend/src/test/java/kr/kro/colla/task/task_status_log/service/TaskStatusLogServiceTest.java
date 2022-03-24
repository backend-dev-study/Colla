package kr.kro.colla.task.task_status_log.service;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.TaskStatusProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import kr.kro.colla.task.task_status_log.domain.repository.TaskStatusLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskStatusLogServiceTest {

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


}