package kr.kro.colla.project.project.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.CreateStoryRequest;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberDecision;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberRequest;
import kr.kro.colla.project.project.presentation.dto.ProjectResponse;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.service.UserProjectService;
import kr.kro.colla.utils.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CookieManager cookieManager;

    @MockBean
    private AuthService authService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserProjectService userProjectService;

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private StoryService storyService;

    private String accessToken = "token";
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        String accessToken = "token";
        loginUser = new LoginUser(345234L);

        given(cookieManager.parseCookies(any(Cookie[].class), eq("accessToken")))
                .willReturn(new Cookie("accessToken", accessToken));
        given(authService.validateAccessToken(eq(accessToken)))
                .willReturn(true);
        given(authService.findUserFromToken(accessToken))
                .willReturn(loginUser);
    }

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
                .cookie(new Cookie("accessToken", this.accessToken))
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
        User user = User.builder()
                .name("subin")
                .githubId(githubId)
                .avatar("github")
                .build();

        given(userService.findByGithubId(githubId))
                .willReturn(user);
        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members")
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberRequest)));
        // then
        perform
                .andExpect(status().isOk());
        verify(noticeService, times(1)).createNotice(any(CreateNoticeRequest.class));
    }

    @Test
    void 사용자_초대를_githubId_부족으로_실패한다() throws Exception {
        // given
        Long projectId = 123142L;
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest();

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members")
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberRequest)));
        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", "githubId").exists());
        verify(noticeService, times(0)).createNotice(any(CreateNoticeRequest.class));
    }

    @Test
    void 사용자가_프로젝트_초대를_거절한다() throws Exception {
        // given
        Long projectId = 123142L;
        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(false);

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members/decision")
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberDecision)));
        // then
        perform
                .andExpect(status().isNoContent());
        verify(userProjectService, times(0)).joinProject(any(User.class), any(Project.class));
    }

    @Test
    void 사용자가_프로젝트_초대를_수락한다() throws Exception {
        // given
        Long projectId = 123142L, userId = loginUser.getId();
        String userName = "subin", userAvatar = "github_contents", userGithubId = "binimini";
        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(true);
        User user = User.builder()
                .name(userName)
                .avatar(userAvatar)
                .githubId(userGithubId)
                .build();
        ReflectionTestUtils.setField(user, "id", userId);
        Project project = Project.builder()
                .name("project_name")
                .build();
        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project)
                .build();

        given(userService.findUserById(any()))
                .willReturn(user);
        given(projectService.findProjectById(projectId))
                .willReturn(project);
        given(userProjectService.joinProject(any(User.class), any(Project.class)))
                .willReturn(userProject);
        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/members/decision")
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectMemberDecision)));
        // then
        perform
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(userId))
                        .andExpect(jsonPath("$.name").value(userName))
                        .andExpect(jsonPath("$.avatar").value(userAvatar))
                        .andExpect(jsonPath("$.githubId").value(userGithubId));
        verify(userProjectService, times(1)).joinProject(any(User.class), any(Project.class));
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

}
