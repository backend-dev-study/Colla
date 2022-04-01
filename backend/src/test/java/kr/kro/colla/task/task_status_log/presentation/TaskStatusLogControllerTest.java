package kr.kro.colla.task.task_status_log.presentation;

import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskStatusLogProvider;
import kr.kro.colla.common.fixture.TaskStatusProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task_status_log.presentation.dto.TaskStatusLogResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.*;

import static kr.kro.colla.common.fixture.TaskStatusLogProvider.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskStatusLogController.class)
class TaskStatusLogControllerTest extends ControllerTest {

    @Test
    void 일주일_단위의_상태_로그를_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Project project = ProjectProvider.createProject(5L);
        Map<String, List<TaskStatusLogResponse>> weeklyTaskStatusLog = new HashMap<>();
        weeklyTaskStatusLog.put("To Do", List.of(
                new TaskStatusLogResponse(태스크_상태_로그_생성(project, TaskStatusProvider.createTaskStatus("To Do"))))
        );
        weeklyTaskStatusLog.put("In Progress", List.of(
                new TaskStatusLogResponse(태스크_상태_로그_생성(project, TaskStatusProvider.createTaskStatus("In Progress"))))
        );
        weeklyTaskStatusLog.put("Done", new ArrayList<>());

        given(taskStatusLogService.getWeeklyTaskStatusLog(eq(projectId)))
                .willReturn(weeklyTaskStatusLog);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/task-status-log")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['To Do'].length()").value(1))
                .andExpect(jsonPath("$['In Progress'].length()").value(1))
                .andExpect(jsonPath("$['Done'].length()").value(0));
        verify(taskStatusLogService, times(1)).getWeeklyTaskStatusLog(anyLong());
    }

}
