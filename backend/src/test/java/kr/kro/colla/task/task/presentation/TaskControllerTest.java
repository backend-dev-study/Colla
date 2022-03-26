package kr.kro.colla.task.task.presentation;

import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.TaskCntByStatus;
import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskResponse;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskSimpleResponse;
import kr.kro.colla.task.task.presentation.dto.UpdateTaskStatusRequest;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.presentation.dto.*;
import kr.kro.colla.task.task.service.converter.TaskResponseConverter;
import kr.kro.colla.user.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest extends ControllerTest {

    @Test
    void 프로젝트에_새로운_태스크를_등록한다() throws Exception {
        // given
        Long taskId = 1L;
        given(taskService.createTask(any(CreateTaskRequest.class)))
                .willReturn(taskId);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/tasks")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", "task title")
                .param("priority", "3")
                .param("status", "To Do")
                .param("projectId", "1"));

        // then
        MvcResult result = perform.andReturn();

        perform.andExpect(status().isCreated());
        assertThat(result.getResponse().getHeader(HttpHeaders.LOCATION)).isEqualTo("/api/projects/tasks/" + taskId);
    }

    @Test
    void 프로젝트에_속한_태스크를_조회한다() throws Exception {
        // given
        Long taskId = 1L;
        ProjectTaskResponse projectTaskResponse = ProjectTaskResponse.builder()
                .id(taskId)
                .title("task title")
                .description("task description")
                .story("user can login with github")
                .preTasks("[]")
                .manager("kykapple")
                .status("In Progress")
                .priority(4)
                .tags(List.of("backend", "frontend"))
                .build();

        given(taskService.getTask(eq(taskId)))
                .willReturn(projectTaskResponse);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/tasks/" + taskId)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(projectTaskResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(projectTaskResponse.getDescription()))
                .andExpect(jsonPath("$.status").value(projectTaskResponse.getStatus()))
                .andExpect(jsonPath("$.priority").exists())
                .andExpect(jsonPath("$.tags").isArray());
    }

    @Test
    void 프로젝트에_속한_테스크의_상태값을_수정한다() throws Exception {
        // given
        Long projectId = 1L, taskId = 13494L;
        String statusNameToUpdate = "새로운~상태~값~입니다~";
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(statusNameToUpdate);

        // when
        ResultActions perform = mockMvc.perform(patch("/projects/" + projectId + "/tasks/" + taskId)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isOk());
        verify(taskService, times(1)).updateTaskStatus(projectId, taskId, statusNameToUpdate);
    }

    @Test
    void 상태값_이름이_부족하면_테스크_상태를_수정할_수_없다() throws Exception {
        // given
        Long projectId = 1L, taskId = 13494L;
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();

        // when
        ResultActions perform = mockMvc.perform(patch("/projects/" + projectId + "/tasks/" + taskId)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.message").value("statusName : must not be null"));
        verify(taskService, times(0)).updateTaskStatus(eq(projectId), eq(taskId), anyString());
    }

    @Test
    void 스토리에_속한_태스크들을_조회한다() throws Exception {
        // given
        Long projectId = 1L, storyId = 5L;
        Project project = ProjectProvider.createProject(7L);
        Story story = StoryProvider.createStory(project, "story title");
        List<RoadmapTaskResponse> taskList = List.of(
                TaskResponseConverter.convertToRoadmapTaskResponse(TaskProvider.createTaskWithTitle(null, project, story, "first task"), null),
                TaskResponseConverter.convertToRoadmapTaskResponse(TaskProvider.createTaskWithTitle(null, project, story, "second task"), null)
        );

        given(taskService.getStoryTasks(eq(projectId), eq(storyId)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/stories/" + storyId + "/tasks")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[*].title").value(containsInAnyOrder(
                        taskList.stream()
                                .map(RoadmapTaskResponse::getTitle)
                                .toArray()))
                );
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_오름차순으로_조회한다() throws Exception {
        // given
        Long projectId = 52494L;
        Project project = ProjectProvider.createProject(92348L);
        List<ProjectTaskSimpleResponse> responses = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTask(null, project, null), null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTask(82349L, project, null), UserProvider.createUser())
        );

        given(taskService.getTasksOrderByCreatedDate(projectId, true))
                .willReturn(responses);
      
        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/created-date?ascending=true")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));
      
        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(responses.size()))
                .andExpect(jsonPath("$[*].title").value(containsInAnyOrder(responses
                        .stream()
                        .map(ProjectTaskSimpleResponse::getTitle)
                        .toArray())))
                .andExpect(jsonPath("$[*].managerName").value(containsInAnyOrder(responses
                        .stream()
                        .map(ProjectTaskSimpleResponse::getManagerName)
                        .toArray())));
        verify(taskService, times(1)).getTasksOrderByCreatedDate(projectId, true);
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_내림차순으로_조회한다() throws Exception {
        // given
        Long projectId = 52494L;
        Project project = ProjectProvider.createProject(92348L);
        List<ProjectTaskSimpleResponse> responses = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTask(null, project, null), null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTask(82349L, project, null), UserProvider.createUser())
        );

        given(taskService.getTasksOrderByCreatedDate(projectId, false))
                .willReturn(responses);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/created-date")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(responses.size()));
        verify(taskService, times(1)).getTasksOrderByCreatedDate(projectId, false);
    }

    @Test
    void 프로젝트의_태스크들을_우선순위_오름차순으로_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Project project = ProjectProvider.createProject(5L);
        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTaskWithPriority(null, project, null, 1), null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTaskWithPriority(null, project, null, 3), null)
        );

        given(taskService.getTasksOrderByPriority(projectId, true))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/priority?ascending=true")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[0].priority").value(1))
                .andExpect(jsonPath("$[1].priority").value(3));
        verify(taskService, times(1)).getTasksOrderByPriority(projectId, true);
    }

    @Test
    void 프로젝트의_태스크들을_우선순위_내림차순으로_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Project project = ProjectProvider.createProject(5L);
        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTaskWithPriority(null, project, null, 5), null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTaskWithPriority(null, project, null, 2), null)
        );

        given(taskService.getTasksOrderByPriority(projectId, false))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/priority")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[*].priority", contains(taskList.get(0).getPriority(), taskList.get(1).getPriority())));
        verify(taskService, times(1)).getTasksOrderByPriority(projectId, false);
    }

    @Test
    void 프로젝트의_테스크들을_상태값들으로_필터링해_조회한다() throws Exception {
        // given
        Long projectId = 23424L, statusId = 9501L;
        Project project = ProjectProvider.createProject(3462L);
        User user = UserProvider.createUser2();
        TaskStatus taskStatus1 = new TaskStatus("To Do");
        TaskStatus taskStatus2 = new TaskStatus("Done");
        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTaskForRepository(null, project, null, taskStatus1), null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(TaskProvider.createTaskForRepository(null, project, null, taskStatus2), user)
        );

        given(taskService.getTasksFilterByStatus(eq(projectId), any(List.class)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/statuses?statuses=To Do, Done")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[*].managerName", contains(null, user.getName())))
                .andExpect(jsonPath("$[*].status", contains(taskStatus1.getName(), taskStatus2.getName())));
        verify(taskService, times(1)).getTasksFilterByStatus(eq(projectId), any(List.class));
    }

    @Test
    void 태그로_필터링_시_선택한_태그들을_포함하는_태스크들을_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Project project = ProjectProvider.createProject(5L);
        List<Tag> tags = List.of(
                TagProvider.createTag("backend"),
                TagProvider.createTag("enhancement"),
                TagProvider.createTag("refactoring")
        );

        Task task1 = TaskProvider.createTask(null, project, null);
        task1.addTags(List.of(
                TaskTagProvider.createTaskTag(task1, tags.get(0)),
                TaskTagProvider.createTaskTag(task1, tags.get(1))
        ));
        Task task2 = TaskProvider.createTask(null, project, null);
        task2.addTags(List.of(
                TaskTagProvider.createTaskTag(task2, tags.get(0)),
                TaskTagProvider.createTaskTag(task2, tags.get(1)),
                TaskTagProvider.createTaskTag(task2, tags.get(2))
        ));

        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task1, null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task2, null)
        );

        given(taskService.getTasksFilterByTags(eq(projectId), any(List.class)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/tags?tags=backend, enhancement")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags").value(containsInAnyOrder("backend", "enhancement")))
                .andExpect(jsonPath("$[1].tags").value(containsInAnyOrder("backend", "enhancement", "refactoring")));
        verify(taskService, times(1)).getTasksFilterByTags(anyLong(), any(List.class));
    }

    @Test
    void 프로젝트의_태스크들을_스토리로_그룹핑한다() throws Exception {
        // given
        Long projectId = 1L;
        Project project = ProjectProvider.createProject(5L);
        Story story1 = StoryProvider.createStory(project, "user can login with github");
        Story story2 = StoryProvider.createStory(project, "write test code");
        Task task1 = TaskProvider.createTask(null, project, story1);
        Task task2 = TaskProvider.createTask(null, project, story2);
        Task task3 = TaskProvider.createTask(null, project, story1);

        List<ProjectTaskSimpleResponse> firstGroupTaskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task1, null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task3, null)
        );
        List<ProjectTaskSimpleResponse> secondGroupTaskList = List.of(TaskResponseConverter.convertToProjectTaskSimpleResponse(task2, null));

        List<ProjectStoryTaskResponse> taskList = List.of(
                new ProjectStoryTaskResponse(story1.getTitle(), firstGroupTaskList),
                new ProjectStoryTaskResponse(story2.getTitle(), secondGroupTaskList)
        );

        given(taskService.getTasksGroupByStory(eq(projectId)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/story")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].story").value(story1.getTitle()))
                .andExpect(jsonPath("$[0].taskList", hasSize(2)))
                .andExpect(jsonPath("$[1].story").value(story2.getTitle()))
                .andExpect(jsonPath("$[1].taskList", hasSize(1)));
    }

    @Test
    void 프로젝트들의_테스크들을_담당자로_필터링해_조회한다() throws Exception {
        // given
        Long projectId = 5553L, managerId1 = 25234L, managerId2 = 6592L;
        Project project = ProjectProvider.createProject(111L);
        User manager = UserProvider.createUser();
        Task task1 = TaskProvider.createTask(managerId1, project, null);
        Task task2 = TaskProvider.createTask(managerId2, project, null);
        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task1, manager),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task2, manager),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task1, manager)
        );

        given(taskService.getTasksFilterByManager(eq(projectId), anyList(), eq(false)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/managers?managers=" + managerId1 + ", " + managerId2)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[*].managerAvatar", contains(manager.getAvatar(), manager.getAvatar(), manager.getAvatar())));
        verify(taskService, times(1)).getTasksFilterByManager(eq(projectId), anyList(), eq(false));
    }

    @Test
    void 프로젝트의_담당자_없는_테스크들을_필터링해_조회한다() throws Exception {
        // given
        Long projectId = 5553L;
        Project project = ProjectProvider.createProject(111L);
        Task task = TaskProvider.createTask(null, project, null);
        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task, null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task, null)
        );

        given(taskService.getTasksFilterByManager(eq(projectId), anyList(), eq(true)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/managers?notSelected=true")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[0].title").value(task.getTitle()))
                .andExpect(jsonPath("$[*].managerAvatar", contains(null, null)))
                .andExpect(jsonPath("$[*].managerName", contains(null, null)));
        verify(taskService, times(1)).getTasksFilterByManager(eq(projectId), anyList(), eq(true));
    }

    @Test
    void 검색한_키워드를_포함하는_제목을_가진_태스크를_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        String keyword = "refactor";
        Project project = ProjectProvider.createProject(5L);
        Task task1 = TaskProvider.createTaskWithTitle(null, project, null, "refactor kanban ui");
        Task task2 = TaskProvider.createTaskWithTitle(null, project, null, "refactor backlog task filter query");

        List<ProjectTaskSimpleResponse> taskList = List.of(
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task1, null),
                TaskResponseConverter.convertToProjectTaskSimpleResponse(task2, null)
        );

        given(taskService.searchTasksByKeyword(eq(projectId), eq(keyword)))
                .willReturn(taskList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/search?keyword=" + keyword)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()))
                .andExpect(jsonPath("$[0].title", containsString(keyword)))
                .andExpect(jsonPath("$[1].title", containsString(keyword)));
    }

    @Test
    void 상태값_별로_테스크_개수를_조회한다() throws Exception {
        // given
        Long projectId = 56234L;
        List<TaskCntResponse> taskCntList = List.of(
                new TaskCntResponse("To Do", 924L),
                new TaskCntResponse("Done", 329L)
        );

        given(taskService.getTaskCntsByStatus(projectId))
                .willReturn(taskCntList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/count")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].taskStatusName").value(taskCntList.get(0).getTaskStatusName()))
                .andExpect(jsonPath("$[0].taskCnt").value(taskCntList.get(0).getTaskCnt()));
    }

    @Test
    void 담당자마다_상태값_별로_테스크_개수를_조회한다() throws Exception {
        // given
        Long projectId = 56234L;
        List<ManagerTaskCntResponse> managerTaskCntList = List.of(
                new ManagerTaskCntResponse("Subin Min", List.of(
                        new TaskCntResponse("To Do", 234L),
                        new TaskCntResponse("Done", 144L)
                )),
                new ManagerTaskCntResponse("YeongKee Kweon", List.of(
                        new TaskCntResponse("Done", 1294L)
                ))
        );

        given(taskService.getTaskCntsByManagerAndStatus(projectId))
                .willReturn(managerTaskCntList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tasks/count/manager")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].managerName").value(managerTaskCntList.get(0).getManagerName()))
                .andExpect(jsonPath("$[0].taskCnts.length()").value(managerTaskCntList.get(0).getTaskCnts().size()))
                .andExpect(jsonPath("$[1].taskCnts[0].taskCnt").value(1294));
    }
}
