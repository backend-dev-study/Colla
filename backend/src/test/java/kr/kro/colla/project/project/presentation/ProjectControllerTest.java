package kr.kro.colla.project.project.presentation;

import com.fasterxml.jackson.databind.type.CollectionType;
import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.exception.exception.project.task_status.TaskStatusAlreadyExistException;
import kr.kro.colla.exception.exception.user.UserNotManagerException;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest extends ControllerTest {

    @Test
    void projectId에_해당하는_프로젝트를_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Map<String, List<ProjectTaskResponse>> tasks = new HashMap<>();
        tasks.put("To Do", new ArrayList<>());
        tasks.put("In Progress", new ArrayList<>());
        tasks.put("Done", new ArrayList<>());
        User user = User.builder()
                .name("kykapple")
                .githubId("kykapple")
                .avatar("github_content")
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(projectId)
                .managerId(loginUser.getId())
                .name("project name")
                .description("project description")
                .thumbnail("s3_content")
                .members(List.of(new UserProfileResponse(user)))
                .tasks(tasks)
                .build();

        given(projectService.getProject(eq(projectId)))
                .willReturn(projectResponse);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(projectResponse.getName()))
                .andExpect(jsonPath("$.description").value(projectResponse.getDescription()))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.tasks").isMap())
                .andExpect(jsonPath("$.tasks.Done").isEmpty());
    }

    @Test
    void 사용자_프로젝트_초대에_성공한다() throws Exception {
        // given
        Long projectId = 123142L;
        String githubId = "binimini";
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest(githubId);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberRequest)));
        // then
        perform
                .andExpect(status().isOk());
        verify(projectService, times(1)).inviteUserToProject(projectId, loginUser.getId(), githubId);
    }

    @Test
    void 사용자_초대를_githubId_부족으로_실패한다() throws Exception {
        // given
        Long projectId = 123142L;
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest();

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberRequest)));
        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", "githubId").exists());
        verify(projectService, times(0)).inviteUserToProject(any(Long.class), any(Long.class), any(String.class));
    }

    @Test
    void 프로젝트_초대를_권한_부족으로_실패한다() throws Exception {
        // given
        Long projectId = 123142L;
        String githubId = "random__github__id";
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest(githubId);

        willThrow(new UserNotManagerException())
                .given(projectService).inviteUserToProject(projectId, loginUser.getId(), githubId);
        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberRequest)));
        // then
        perform
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("403"))
                .andExpect(jsonPath("$.message").value(new UserNotManagerException().getMessage()));
        verify(projectService, times(1)).inviteUserToProject(projectId, loginUser.getId(), githubId);
    }

    @Test
    void 사용자가_프로젝트_초대를_거절한다() throws Exception {
        // given
        Long projectId = 123142L, noticeId = 64232L;
        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(false, noticeId);

        given(projectService.decideInvitation(eq(projectId), eq(loginUser.getId()), any(ProjectMemberDecision.class)))
                .willReturn(null);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members/decision")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberDecision)));

        // then
        perform
                .andExpect(status().isOk());
        verify(projectService, times(1)).decideInvitation(eq(projectId), eq(loginUser.getId()), any(ProjectMemberDecision.class));
    }

    @Test
    void 사용자가_프로젝트_초대를_수락한다() throws Exception {
        // given
        Long projectId = 123142L, userId = loginUser.getId(), noticeId = 63452L;
        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(true, noticeId);
        User user = User.builder()
                .name("random user name")
                .avatar("random user avatar")
                .githubId("random github id")
                .build();
        ReflectionTestUtils.setField(user, "id", userId);

        given(projectService.decideInvitation(eq(projectId), eq(loginUser.getId()), any(ProjectMemberDecision.class)))
                .willReturn(new ProjectMemberResponse(user));

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members/decision")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberDecision)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.avatar").value(user.getAvatar()))
                .andExpect(jsonPath("$.githubId").value(user.getGithubId()));
        verify(projectService, times(1)).decideInvitation(eq(projectId), eq(loginUser.getId()), any(ProjectMemberDecision.class));
    }

    @Test
    void 프로젝트_스토리를_생성한다() throws Exception {
        // given
        Long projectId = 1L;
        String title = "story title";
        Story story = Story.builder()
                .title(title)
                .preStories("[]")
                .build();
        String content = objectMapper.writeValueAsString(new CreateStoryRequest(title));

        given(storyService.createStory(eq(projectId), any(CreateStoryRequest.class)))
                .willReturn(story);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/stories")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title));
        verify(storyService, times(1)).createStory(eq(projectId), any(CreateStoryRequest.class));
    }

    @Test
    void 스토리_제목이_없다면_스토리_생성에_실패한다() throws Exception {
        // given
        Long projectId = 1L;
        String content = objectMapper.writeValueAsString(new CreateStoryRequest(""));

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/stories")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("title : must not be blank"));
        verify(storyService, never()).createStory(eq(projectId), any(CreateStoryRequest.class));
    }

    @Test
    void 프로젝트의_스토리를_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Story story = Story.builder()
                .title("story title")
                .preStories("[]")
                .build();
        List<ProjectStorySimpleResponse> projectStorySimpleRespons = List.of(new ProjectStorySimpleResponse(story));

        given(projectService.getProjectStories(projectId))
                .willReturn(projectStorySimpleRespons);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/stories")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = perform.andReturn();
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ProjectStorySimpleResponse.class);
        List<ProjectStorySimpleResponse> projectStorySimpleResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(story.getTitle()));
        assertThat(projectStorySimpleResponseList.size()).isEqualTo(1);
    }

    @Test
    void 프로젝트_멤버를_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        User user = User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("github_content")
                .build();
        List<ProjectMemberResponse> projectMemberResponses = List.of(new ProjectMemberResponse(user));

        given(projectService.getProjectMembers(projectId))
                .willReturn(projectMemberResponses);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/members")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = perform.andReturn();
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ProjectMemberResponse.class);
        List<ProjectMemberResponse> projectMemberResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].avatar").value(user.getAvatar()));
        assertThat(projectMemberResponseList).hasSize(1);
    }

    @Test
    void 프로젝트에서_사용할_태그를_생성한다() throws Exception {
        // given
        Long projectId = 1L;
        String tagName = "backend";
        Tag tag = new Tag(tagName);
        String content = objectMapper.writeValueAsString(new CreateTagRequest(tagName));

        given(projectService.createTag(eq(projectId), any(CreateTagRequest.class)))
                .willReturn(tag);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/tags")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tag.getName()));
    }

    @Test
    void 태그의_제목이_없다면_태그_생성에_실패한다() throws Exception {
        // given
        Long projectId = 1L;
        String content = objectMapper.writeValueAsString(new CreateTagRequest(""));

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/tags")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name : must not be blank"));
        verify(projectService, never()).createTag(eq(projectId), any(CreateTagRequest.class));
    }

    @Test
    void 프로젝트에_등록되어_있는_테스크_태그들을_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Tag backendTag = new Tag("backend");
        Tag frontendTag = new Tag("frontend");
        Tag refactoringTag = new Tag("refactoring");
        List<ProjectTagResponse> projectTagResponses = List.of(
                new ProjectTagResponse(backendTag),
                new ProjectTagResponse(frontendTag),
                new ProjectTagResponse(refactoringTag)
        );

        given(projectService.getProjectTags(projectId))
                .willReturn(projectTagResponses);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/tags")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = perform.andReturn();
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ProjectTagResponse.class);
        List<ProjectTagResponse> projectTagResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);

        perform
                .andExpect(status().isOk());
        assertThat(projectTagResponseList.size()).isEqualTo(3);
        assertThat(projectTagResponseList).extracting("name")
                .contains("backend", "frontend", "refactoring");
    }

    @Test
    void 프로젝트의_테스크_상태_추가에_성공한다() throws Exception {
        // given
        Long projectId = 72434L, taskStatusId = 35829L;
        String statusName = "새로 생성할 테스크 상태값";
        CreateTaskStatusRequest request = new CreateTaskStatusRequest(statusName);
        TaskStatus taskStatus = new TaskStatus(statusName);
        ReflectionTestUtils.setField(taskStatus, "id", taskStatusId);

        given(projectService.createTaskStatus(projectId, statusName))
                .willReturn(taskStatus);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/statuses")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskStatus.getId().intValue()))
                .andExpect(jsonPath("$.name").value(taskStatus.getName()));
        verify(projectService, times(1)).createTaskStatus(projectId, statusName);
    }

    @Test
    void 존재하는_이름의_프로젝트_테스크_상태_추가에_실패한다() throws Exception {
        // given
        Long projectId = 23425L;
        String taskStatusName = "already_exist_task_status_name!!";
        CreateTaskStatusRequest request = new CreateTaskStatusRequest(taskStatusName);

        willThrow(new TaskStatusAlreadyExistException()).
                given(projectService).createTaskStatus(projectId, taskStatusName);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/statuses")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(new TaskStatusAlreadyExistException().getMessage()));
    }

    @Test
    void 상태의_이름이_없다면_프로젝트의_테스크_상태_추가에_실패한다() throws Exception {
        // given
        Long projectId = 72434L;
        CreateTaskStatusRequest request = new CreateTaskStatusRequest();
        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/statuses")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name : must not be null"));
        verify(projectService, times(0)).createTaskStatus(eq(projectId), anyString());
    }

    @Test
    void 프로젝트의_테스크_상태_삭제에_성공한다() throws Exception {
        // given
        Long projectId = 72434L;
        String statusName = "삭제할 테스크 상태값", statusNameToChange = "변경할 테스크 상태값";
        DeleteTaskStatusRequest request = new DeleteTaskStatusRequest(statusName, statusNameToChange);

        // when
        ResultActions perform = mockMvc.perform(delete("/projects/" + projectId + "/statuses")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTaskStatus(projectId, statusName, statusNameToChange);
    }

    @Test
    void 상태의_이름이_없다면_프로젝트의_테스크_상태_삭제에_실패한다() throws Exception {
        // given
        Long projectId = 72434L;
        DeleteTaskStatusRequest request = new DeleteTaskStatusRequest();
        // when
        ResultActions perform = mockMvc.perform(delete("/projects/" + projectId + "/statuses")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", anyOf(is("from : must not be null"), is("to : must not be null"))));
        verify(taskService, times(0)).deleteTaskStatus(eq(projectId), anyString(), anyString());
    }

    @Test
    void 프로젝트의_테스크_상태_조회에_성공한다() throws Exception {
        // given
        Long projectId = 435345L;
        List<TaskStatus> taskStatuses = List.of(
                new TaskStatus("status1"),
                new TaskStatus("status2"),
                new TaskStatus("status3")
        );
        List<ProjectTaskStatusResponse> responses = taskStatuses
                .stream()
                .map(ProjectTaskStatusResponse::new)
                .collect(Collectors.toList());

        given(projectService.getTaskStatuses(projectId))
                .willReturn(responses);
        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/statuses")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("status1", "status2", "status3")));
    }
}
