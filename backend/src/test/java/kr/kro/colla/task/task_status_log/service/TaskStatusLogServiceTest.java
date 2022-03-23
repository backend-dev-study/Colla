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

import java.util.Optional;

import static kr.kro.colla.common.fixture.TaskStatusLogProvider.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskStatusLogServiceTest {

    @Mock
    private TaskStatusLogRepository taskStatusLogRepository;

    @InjectMocks
    private TaskStatusLogService taskStatusLogService;

    @Test
    void 태스크_생성_및_수정_시_상태_로그를_기록한다() {
        // given
        Project project = ProjectProvider.createProject(5L);
        Task task = TaskProvider.createTask(null, project, null);
        TaskStatus taskStatus = TaskStatusProvider.createTaskStatus("To Do");

        // when
        taskStatusLogService.writeTaskStatusLog(project, task, null, taskStatus);

        // then
        verify(taskStatusLogRepository, times(1)).save(any(TaskStatusLog.class));
        verify(taskStatusLogRepository, never()).findTaskStatusLogByProjectAndTaskAndStatus(any(Project.class), any(Task.class), anyString());
    }

    @Test
    void 태스크_상태가_완료에서_다른_상태로_변경될_경우_완료_상태_로그를_삭제한다() {
        // given
        Project project = ProjectProvider.createProject(5L);
        Task task = TaskProvider.createTask(null, project, null);
        TaskStatus prevStatus = TaskStatusProvider.createTaskStatus("Done");
        TaskStatus newStatus = TaskStatusProvider.createTaskStatus("In Progress");
        TaskStatusLog taskStatusLog = 태스크_상태_로그_생성(project, task, prevStatus);

        given(taskStatusLogRepository.findTaskStatusLogByProjectAndTaskAndStatus(any(Project.class), any(Task.class), anyString()))
                .willReturn(Optional.ofNullable(taskStatusLog));

        // when
        taskStatusLogService.writeTaskStatusLog(project, task, prevStatus, newStatus);

        // then
        verify(taskStatusLogRepository, never()).save(any(TaskStatusLog.class));
        verify(taskStatusLogRepository, times(1)).findTaskStatusLogByProjectAndTaskAndStatus(any(Project.class), any(Task.class), anyString());
        verify(taskStatusLogRepository, times(1)).delete(any(TaskStatusLog.class));
    }

}
